package com.example.practicadoskotlin.ui.registro

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.practicadoskotlin.modelo.ClienteDTO
import com.example.practicadoskotlin.ui.validadores.validarDni
import com.example.practicadoskotlin.ui.validadores.validarEmail
import com.example.practicadoskotlin.ui.validadores.validarNombre
import com.example.practicadoskotlin.ui.validadores.validarPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegistroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun onDniChange(valor: String) = actualizar { it.copy(dni = valor) }
    fun onNombreChange(valor: String) = actualizar { it.copy(nombre = valor) }
    fun onDireccionChange(valor: String) = actualizar { it.copy(direccion = valor) }
    fun onTelefonoChange(valor: String) = actualizar { it.copy(telefono = valor) }
    fun onEmailChange(valor: String) = actualizar { it.copy(email = valor) }
    fun onPasswordChange(valor: String) = actualizar { it.copy(password = valor) }
    fun onConfirmarChange(valor: String) = actualizar { it.copy(confirmar = valor) }

    fun onTogglePasswordVisible() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onRegistrarClick() {
        val s = _uiState.value
        if (!s.botonHabilitado) return

        val usuarioDto = ClienteDTO(
            id = 0,
            dni = s.dni,
            nombre = s.nombre,
            direccion = s.direccion,
            telefono = s.telefono,
            email = s.email,
            password = s.password
        )
        Log.d("Registro", usuarioDto.toString())
        _uiState.update { it.copy(registroCompletado = true) }
    }

    private fun actualizar(cambio: (RegistroUiState) -> RegistroUiState) {
        _uiState.update { validar(cambio(it)) }
    }

    private fun validar(s: RegistroUiState): RegistroUiState {
        val dniError = validarDni(s.dni)
        val nombreError = validarNombre(s.nombre)
        val emailError = validarEmail(s.email)
        val passwordError = validarPassword(s.password)
        val confirmError =
            if (s.confirmar.isNotEmpty() && s.confirmar != s.password)
                "Las contraseñas no coinciden" else null

        val todosRellenos = listOf(
            s.dni, s.nombre, s.direccion, s.telefono, s.email, s.password, s.confirmar
        ).all { it.isNotBlank() }

        val sinErrores = listOf(
            dniError, nombreError, emailError, passwordError, confirmError
        ).all { it == null }

        return s.copy(
            dniError = dniError,
            nombreError = nombreError,
            emailError = emailError,
            passwordError = passwordError,
            confirmError = confirmError,
            botonHabilitado = todosRellenos && sinErrores
        )
    }
}