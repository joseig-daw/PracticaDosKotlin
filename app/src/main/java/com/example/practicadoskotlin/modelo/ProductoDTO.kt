package com.example.practicadoskotlin.modelo

sealed interface ProductoDTO {
    val id: Int
    val nombre: String
    val precio: Double
}