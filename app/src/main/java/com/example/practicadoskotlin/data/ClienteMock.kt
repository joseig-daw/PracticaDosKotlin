package com.example.practicadoskotlin.data

import com.example.practicadoskotlin.modelo.ClienteDTO

object ClienteMock {
    val cliente = ClienteDTO(
        id = 1,
        dni = "12345678Z",
        nombre = "Cliente Demo",
        direccion = "Calle Mayor 1, Valencia",
        telefono = "600123456",
        email = "demo@correo.com",
        password = "1234"
    )
}