package com.example.practicadoskotlin.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import com.example.practicadoskotlin.R
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.RamenDining
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.practicadoskotlin.modelo.BebidaDTO
import com.example.practicadoskotlin.modelo.PastaDTO
import com.example.practicadoskotlin.modelo.PizzaDTO
import com.example.practicadoskotlin.modelo.ProductoDTO
import com.example.practicadoskotlin.modelo.SIZE

enum class TipoProducto(val titulo: String, val singular: String) {
    PIZZA("Pizzas", "Pizza"),
    PASTA("Pastas", "Pasta"),
    BEBIDA("Bebidas", "Bebida")
}

// Qué tipo es cada producto (el when es exhaustivo gracias a la sealed interface)
fun ProductoDTO.tipo(): TipoProducto = when (this) {
    is PizzaDTO -> TipoProducto.PIZZA
    is PastaDTO -> TipoProducto.PASTA
    is BebidaDTO -> TipoProducto.BEBIDA
}

data class EstiloTipo(
    val icono: ImageVector,
    @DrawableRes val imagen: Int,
    val fondo: Color,
    val contenido: Color
)

// Colores del tema: se adaptan solos al tema que generaste con Material Theme Builder
@Composable
fun TipoProducto.estilo(): EstiloTipo {
    val c = MaterialTheme.colorScheme
    return when (this) {
        TipoProducto.PIZZA -> EstiloTipo(
            Icons.Filled.LocalPizza, R.drawable.pizza,
            c.primaryContainer, c.onPrimaryContainer
        )
        TipoProducto.PASTA -> EstiloTipo(
            Icons.Filled.RamenDining, R.drawable.pasta,
            c.primaryContainer, c.onSecondaryContainer
        )
        TipoProducto.BEBIDA -> EstiloTipo(
            Icons.Filled.LocalDrink, R.drawable.bebida,
            c.tertiaryContainer, c.onTertiaryContainer
        )
    }
}

// Mismo SIZE, pero con el nombre que tiene sentido para cada tipo de producto
fun SIZE.etiquetaPara(tipo: TipoProducto): String = when (tipo) {
    TipoProducto.PIZZA -> when (this) {
        SIZE.PEQUENIO -> "Pequeña"
        SIZE.MEDIANO -> "Mediana"
        SIZE.GRANDE -> "Familiar"
    }
    TipoProducto.PASTA -> when (this) {
        SIZE.PEQUENIO -> "Individual"
        SIZE.MEDIANO -> "Normal"
        SIZE.GRANDE -> "compartir"
    }
    TipoProducto.BEBIDA -> when (this) {
        SIZE.PEQUENIO -> "33 cl"
        SIZE.MEDIANO -> "50 cl"
        SIZE.GRANDE -> "1 L"
    }
}