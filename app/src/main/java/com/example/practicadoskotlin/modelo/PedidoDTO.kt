package com.example.practicadoskotlin.modelo

import com.example.practicadoskotlin.modelo.ClienteDTO
import java.util.Date

data class PedidoDTO(
    val id:Int,
    val fecha: Date,
    var precioTotal: Float,
    val estado: EstadoPedido,
    val cliente: ClienteDTO,
    val lineaPedidos: MutableList<LineaPedidoDTO> = mutableListOf()
)
