package com.example.practicadoskotlin.ui.registro

data class RegistroUiState(
    val dni: String = "",
    val nombre: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val email: String = "",
    val password: String = "",
    val confirmar: String = "",

    val passwordVisible: Boolean = false,

    val dniError: String? = null,
    val nombreError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmError: String? = null,

    val botonHabilitado: Boolean = false,
    val registroCompletado: Boolean = false
)