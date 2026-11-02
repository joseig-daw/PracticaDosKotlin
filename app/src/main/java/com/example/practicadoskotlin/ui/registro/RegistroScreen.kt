package com.example.practicadoskotlin.ui.registro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicadoskotlin.R

@Composable
fun RegistroScreen(viewModel: RegistroViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val transformacion =
        if (uiState.passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(mask = '*')

    val iconoOjo: @Composable () -> Unit = {
        IconButton(onClick = viewModel::onTogglePasswordVisible) {
            Icon(
                imageVector = if (uiState.passwordVisible) Icons.Filled.VisibilityOff
                else Icons.Filled.Visibility,
                contentDescription = if (uiState.passwordVisible) "Ocultar contraseña"
                else "Mostrar contraseña"
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.pizza),
            contentDescription = "Logo de la pizzería",
            modifier = Modifier
                .fillMaxWidth()
                .size(120.dp)
                .padding(all = 30.dp),
            alignment = Alignment.TopCenter
        )
        Text("Registro", style = MaterialTheme.typography.titleLarge)

        CampoTexto(uiState.dni, viewModel::onDniChange, "DNI", uiState.dniError)
        CampoTexto(uiState.nombre, viewModel::onNombreChange, "Nombre", uiState.nombreError)
        CampoTexto(uiState.direccion, viewModel::onDireccionChange, "Dirección", null)
        CampoTexto(
            uiState.telefono, viewModel::onTelefonoChange, "Teléfono", null,
            keyboardType = KeyboardType.Phone
        )
        CampoTexto(
            uiState.email, viewModel::onEmailChange, "Email", uiState.emailError,
            keyboardType = KeyboardType.Email
        )
        CampoTexto(
            uiState.password, viewModel::onPasswordChange, "Contraseña", uiState.passwordError,
            keyboardType = KeyboardType.Password,
            visualTransformation = transformacion,
            trailingIcon = iconoOjo
        )
        CampoTexto(
            uiState.confirmar, viewModel::onConfirmarChange, "Confirmar contraseña",
            uiState.confirmError,
            keyboardType = KeyboardType.Password,
            visualTransformation = transformacion,
            trailingIcon = iconoOjo
        )

        Button(
            onClick = viewModel::onRegistrarClick,
            enabled = uiState.botonHabilitado,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Registrarse") }
    }
}

@Composable
fun CampoTexto(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = { if (error != null) Text(error) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        modifier = modifier.fillMaxWidth()
    )
}