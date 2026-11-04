package com.example.practicadoskotlin.modelo

data class PastaDTO(
    override val id: Int,
    override val nombre: String,
    override val precio: Double,
    val lineaPedido: LineaPedidoDTO? = null,
    val ingredientes: MutableList<IngredienteDTO>
) : ProductoDTO
