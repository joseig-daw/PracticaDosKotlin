package com.example.practicadoskotlin.ui.login


data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val botonHabilitado: Boolean = false
)