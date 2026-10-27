package com.example.pizzeria.modelo

import java.util.Date

data class PedidoDTO(
    val id:Int,
    val fecha: Date,
    val precioTotal: Float,
    val estado: EstadoPedido,
    val cliente: ClienteDTO,
    val lineaPedidos: List<LineaPedidoDTO> = mutableListOf()
)
