package com.example.practicadoskotlin.data

import com.example.practicadoskotlin.modelo.BebidaDTO
import com.example.practicadoskotlin.modelo.IngredienteDTO
import com.example.practicadoskotlin.modelo.PastaDTO
import com.example.practicadoskotlin.modelo.PizzaDTO
import com.example.practicadoskotlin.modelo.SIZE

object ProductosMock {

    // Ingredientes reutilizables
    private val masa = IngredienteDTO(1, "Masa de trigo", listOf("Gluten"))
    private val tomate = IngredienteDTO(2, "Tomate", emptyList())
    private val mozzarella = IngredienteDTO(3, "Mozzarella", listOf("Lácteos"))
    private val albahaca = IngredienteDTO(4, "Albahaca", emptyList())
    private val ternera = IngredienteDTO(5, "Ternera", emptyList())
    private val bacon = IngredienteDTO(6, "Bacon", emptyList())
    private val gorgonzola = IngredienteDTO(7, "Gorgonzola", listOf("Lácteos"))
    private val parmesano = IngredienteDTO(8, "Parmesano", listOf("Lácteos"))
    private val espagueti = IngredienteDTO(9, "Espagueti", listOf("Gluten"))
    private val tallarin = IngredienteDTO(10, "Tallarín", listOf("Gluten"))
    private val fusilli = IngredienteDTO(11, "Fusilli", listOf("Gluten"))
    private val nata = IngredienteDTO(12, "Nata", listOf("Lácteos"))
    private val huevo = IngredienteDTO(13, "Huevo", listOf("Huevo"))
    private val pesto = IngredienteDTO(14, "Pesto", listOf("Frutos secos"))

    val pizzas = listOf(
        PizzaDTO(1, "Margarita", 8.50, SIZE.MEDIANO,
            ingredientes = mutableListOf(masa, tomate, mozzarella, albahaca)),
        PizzaDTO(2, "Barbacoa", 10.90, SIZE.MEDIANO,
            ingredientes = mutableListOf(masa, ternera, bacon, mozzarella)),
        PizzaDTO(3, "Cuatro quesos", 10.50, SIZE.MEDIANO,
            ingredientes = mutableListOf(masa, mozzarella, gorgonzola, parmesano))
    )

    val pastas = listOf(
        PastaDTO(11, "Carbonara", 9.00,
            ingredientes = mutableListOf(espagueti, nata, huevo, bacon)),
        PastaDTO(12, "Boloñesa", 9.50,
            ingredientes = mutableListOf(tallarin, ternera, tomate)),
        PastaDTO(13, "Pesto", 8.90,
            ingredientes = mutableListOf(fusilli, pesto, parmesano))
    )

    val bebidas = listOf(
        BebidaDTO(21, "Cola", 2.50, SIZE.MEDIANO),
        BebidaDTO(22, "Agua mineral", 1.50, SIZE.MEDIANO),
        BebidaDTO(23, "Cerveza", 3.00, SIZE.MEDIANO)
    )
}