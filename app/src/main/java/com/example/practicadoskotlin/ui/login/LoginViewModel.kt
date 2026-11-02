package com.example.practicadoskotlin.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.practicadoskotlin.modelo.LoginDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(valor: String) {
        _uiState.update { recalcular(it.copy(email = valor)) }
    }

    fun onPasswordChange(valor: String) {
        _uiState.update { recalcular(it.copy(password = valor)) }
    }

    fun onTogglePasswordVisible() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onLoginClick() {
        val s = _uiState.value
        if (!s.botonHabilitado) return

        val loginDto = LoginDTO(email = s.email, password = s.password)
        Log.d("Login", loginDto.toString())
    }

    private fun recalcular(s: LoginUiState): LoginUiState =
        s.copy(botonHabilitado = s.email.isNotBlank() && s.password.isNotBlank())
}