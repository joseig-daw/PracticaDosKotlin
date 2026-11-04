package com.example.practicadoskotlin.modelo

data class BebidaDTO(
    override val id: Int,
    override val nombre: String,
    override val precio: Double,
    val size: SIZE,
    val lineaPedido: LineaPedidoDTO? = null
) : ProductoDTO