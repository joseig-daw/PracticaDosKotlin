package com.example.pizzeria.modelo

data class PastaDTO(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val lineaPedido: LineaPedidoDTO?=null,
    val ingredientes: MutableList<IngredienteDTO>
)
