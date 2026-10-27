package com.example.pizzeria.modelo

data class ClienteDTO(
    val id: Int,
    val dni: String,
    val nombre: String,
    val direccio: String,
    val telefono: String,
    val email: String,
    val password: String,
    val pedidos: List<PedidoDTO> = emptyList()
)
