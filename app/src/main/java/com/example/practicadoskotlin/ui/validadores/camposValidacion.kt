package com.example.practicadoskotlin.ui.validadores
private val EMAIL_REGEX =
    Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

private const val LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE"

fun validarNombre(nombre: String): String? {
    if (nombre.isEmpty()) return null
    return if (nombre.any { it.isDigit() }) "El nombre no puede contener dígitos" else null
}

fun validarEmail(email: String): String? {
    if (email.isEmpty()) return null
    return if (!EMAIL_REGEX.matches(email)) "El email no tiene un formato válido" else null
}

fun validarPassword(password: String): String? {
    if (password.isEmpty()) return null
    return if (password.length < 4) "La contraseña debe tener al menos 4 caracteres" else null
}

fun validarDni(dni: String): String? {
    if (dni.isEmpty()) return null
    if (dni.length != 9) return "El DNI debe tener 9 caracteres (8 números y una letra)"

    val numeros = dni.substring(0, 8)
    val letra = dni[8].uppercaseChar()

    if (!numeros.all { it.isDigit() }) return "Los 8 primeros caracteres deben ser números"
    if (!letra.isLetter()) return "El último carácter debe ser una letra"

    val letraCorrecta = LETRAS_DNI[numeros.toInt() % 23]
    return if (letra != letraCorrecta) "La letra del DNI no es correcta" else null
}