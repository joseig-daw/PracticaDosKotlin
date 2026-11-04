package com.example.practicadoskotlin.ui.home

import com.example.practicadoskotlin.modelo.SIZE

fun SIZE.etiqueta(): String = when (this) {
    SIZE.PEQUENIO -> "Pequeño"
    SIZE.MEDIANO -> "Mediano"
    SIZE.GRANDE -> "Grande"
}

// El precio del DTO se considera el del tamaño MEDIANO
fun SIZE.factor(): Double = when (this) {
    SIZE.PEQUENIO -> 0.8
    SIZE.MEDIANO -> 1.0
    SIZE.GRANDE -> 1.3
}