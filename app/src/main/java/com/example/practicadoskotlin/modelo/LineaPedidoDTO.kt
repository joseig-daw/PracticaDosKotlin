package com.example.pizzeria.modelo

data class LineaPedidoDTO(
    val id: Int,
    val cantidad: Int,
    val pedido: PedidoDTO,
    val pizza: PizzaDTO? = null,
    val pasta: PastaDTO? = null,
    val bebida: BebidaDTO? = null
)
