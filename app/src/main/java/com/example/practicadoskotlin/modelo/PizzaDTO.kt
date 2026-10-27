package com.example.pizzeria.modelo

data class PizzaDTO(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val size: SIZE,
    val lineaPedido: LineaPedidoDTO?=null,
    val ingredientes: MutableList<IngredienteDTO> = mutableListOf()
)
