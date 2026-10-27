package com.example.pizzeria.modelo

data class BebidaDTO(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val size: SIZE,
    val lineaPedido: LineaPedidoDTO?=null
)
