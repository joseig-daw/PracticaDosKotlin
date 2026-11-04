package com.example.practicadoskotlin.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicadoskotlin.modelo.ProductoDTO
import com.example.practicadoskotlin.modelo.SIZE
import com.example.practicadoskotlin.data.ProductosMock
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            delay(1000) // simula la espera de una llamada a un servidor

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

    // ----- Lógica interna -----
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