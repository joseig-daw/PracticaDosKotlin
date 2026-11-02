package com.example.practicadoskotlin.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun LoginScreen(
    onGoToRegister: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
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
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
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
        Text("Iniciar sesión", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Contraseña") },
            visualTransformation = transformacion,
            trailingIcon = iconoOjo,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = viewModel::onLoginClick,
            enabled = uiState.botonHabilitado,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Login") }

        TextButton(onClick = onGoToRegister) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}