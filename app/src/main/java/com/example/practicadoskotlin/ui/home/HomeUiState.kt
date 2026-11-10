package com.example.practicadoskotlin.ui.home

import com.example.practicadoskotlin.modelo.BebidaDTO
import com.example.practicadoskotlin.modelo.PastaDTO
import com.example.practicadoskotlin.modelo.PizzaDTO
import com.example.practicadoskotlin.modelo.ProductoDTO
import com.example.practicadoskotlin.modelo.SIZE

data class ItemCatalogo(
    val producto: ProductoDTO,
    val cantidad: Int = 1,
    val size: SIZE = SIZE.MEDIANO
) {
    val precioUnitario: Double get() = producto.precio * size.factor()
    val subtotal: Double get() = precioUnitario * cantidad
}

data class HomeUiState(
    val cargando: Boolean = true,
    val items: List<ItemCatalogo> = emptyList(),

    val totalProductos: Int = 0,
    val productosAnadidos: Int? = null
) {
    val pizzas: List<ItemCatalogo> get() = items.filter { it.producto is PizzaDTO }
    val pastas: List<ItemCatalogo> get() = items.filter { it.producto is PastaDTO }
    val bebidas: List<ItemCatalogo> get() = items.filter { it.producto is BebidaDTO }
}