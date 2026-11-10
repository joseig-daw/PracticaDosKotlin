package com.example.practicadoskotlin.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicadoskotlin.data.ClienteMock
import com.example.practicadoskotlin.data.ProductosMock
import com.example.practicadoskotlin.modelo.BebidaDTO
import com.example.practicadoskotlin.modelo.EstadoPedido
import com.example.practicadoskotlin.modelo.LineaPedidoDTO
import com.example.practicadoskotlin.modelo.PastaDTO
import com.example.practicadoskotlin.modelo.PedidoDTO
import com.example.practicadoskotlin.modelo.PizzaDTO
import com.example.practicadoskotlin.modelo.ProductoDTO
import com.example.practicadoskotlin.modelo.SIZE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // El pedido actual (el "carrito"). Vive aquí, no en el UiState.
    private val pedido = PedidoDTO(
        id = 1,
        fecha = Date(),
        precioTotal = 0f,
        estado = EstadoPedido.PENDIENTE,
        cliente = ClienteMock.cliente
    )
    private var siguienteIdLinea = 1

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            delay(1000)

            val productos = buildList<ProductoDTO> {
                addAll(ProductosMock.pizzas)
                addAll(ProductosMock.pastas)
                addAll(ProductosMock.bebidas)
            }
            _uiState.update {
                it.copy(cargando = false, items = productos.map { p -> ItemCatalogo(p) })
            }
        }
    }

    // ----- Eventos desde la UI -----
    fun onCantidadChange(id: Int, nuevaCantidad: Int) =
        actualizarItem(id) { it.copy(cantidad = nuevaCantidad.coerceIn(1, 10)) }

    fun onSizeChange(id: Int, nuevoSize: SIZE) =
        actualizarItem(id) { it.copy(size = nuevoSize) }

    fun onAnadirClick(id: Int) {
        val item = _uiState.value.items.firstOrNull { it.producto.id == id } ?: return

        // 1) Construir la LineaPedido y añadirla al Pedido
        val linea = construirLinea(item)
        pedido.lineaPedidos.add(linea)
        pedido.precioTotal = pedido.lineaPedidos.sumOf { it.subtotal() }.toFloat()

        // 2) Mostrar ambos objetos en Logcat
        Log.d("LineaPedido", linea.toString())
        Log.d("Pedido", pedido.toString())

        // 3) Publicar el nuevo estado: badge + aviso del Toast
        _uiState.update {
            it.copy(
                totalProductos = pedido.lineaPedidos.sumOf { l -> l.cantidad },
                productosAnadidos = item.cantidad
            )
        }
    }

    fun onToastMostrado() {
        _uiState.update { it.copy(productosAnadidos = null) }
    }

    // ----- Lógica interna -----
    private fun construirLinea(item: ItemCatalogo): LineaPedidoDTO {
        val idLinea = siguienteIdLinea++
        return when (val p = item.producto) {
            is PizzaDTO -> LineaPedidoDTO(
                id = idLinea, cantidad = item.cantidad, pedido = pedido,
                pizza = p.copy(size = item.size, precio = item.precioUnitario)
            )
            is PastaDTO -> LineaPedidoDTO(
                id = idLinea, cantidad = item.cantidad, pedido = pedido,
                pasta = p.copy(precio = item.precioUnitario)
            )
            is BebidaDTO -> LineaPedidoDTO(
                id = idLinea, cantidad = item.cantidad, pedido = pedido,
                bebida = p.copy(size = item.size, precio = item.precioUnitario)
            )
        }
    }

    private fun LineaPedidoDTO.subtotal(): Double {
        val precioUnitario = pizza?.precio ?: pasta?.precio ?: bebida?.precio ?: 0.0
        return precioUnitario * cantidad
    }

    private fun actualizarItem(id: Int, cambio: (ItemCatalogo) -> ItemCatalogo) {
        _uiState.update { estado ->
            estado.copy(
                items = estado.items.map { item ->
                    if (item.producto.id == id) cambio(item) else item
                }
            )
        }
    }
}