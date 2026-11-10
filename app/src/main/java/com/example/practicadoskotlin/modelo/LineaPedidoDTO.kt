package com.example.practicadoskotlin.modelo

data class LineaPedidoDTO(
    val id: Int,
    val cantidad: Int,
    val pedido: PedidoDTO,
    val pizza: PizzaDTO? = null,
    val pasta: PastaDTO? = null,
    val bebida: BebidaDTO? = null
){
    // Evitan el bucle infinito Pedido -> Línea -> Pedido
    override fun toString(): String =
        "LineaPedidoDTO(id=$id, cantidad=$cantidad, pedidoId=${pedido.id}, " +
                "pizza=$pizza, pasta=$pasta, bebida=$bebida)"

    override fun equals(other: Any?): Boolean = other is LineaPedidoDTO && other.id == id

    override fun hashCode(): Int = id
}
