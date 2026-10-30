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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pizzeria.modelo.ClienteDTO
import com.example.practicadoskotlin.R
import com.example.practicadoskotlin.ui.validadores.validarDni
import com.example.practicadoskotlin.ui.validadores.validarEmail
import com.example.practicadoskotlin.ui.validadores.validarNombre
import com.example.practicadoskotlin.ui.validadores.validarPassword

@Composable
fun RegistroScreen(onRegister: (ClienteDTO) -> Unit = {}) {
    var dni by rememberSaveable { mutableStateOf("") }
    var nombre by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }



    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val transformacion =
        if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(mask='*')

    val iconoOjo: @Composable () -> Unit = {
        IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(
                imageVector = if (passwordVisible) Icons.Filled.VisibilityOff
                else Icons.Filled.Visibility,
                contentDescription = if (passwordVisible) "Ocultar contraseña"
                else "Mostrar contraseña"
            )
        }
    }
    val dniError = validarDni(dni)
    val nombreError = validarNombre(nombre)
    val emailError = validarEmail(email)
    val passwordError = validarPassword(password)
    val confirmError = if (confirmar.isNotEmpty() && confirmar != password)
        "Las contraseñas no coinciden" else null

    val formularioValido =
        listOf(dni, nombre, direccion, telefono, email, password, confirmar).all { it.isNotBlank() } &&
                dniError == null && nombreError == null && emailError == null &&
                passwordError == null && confirmError == null
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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

        OutlinedTextField(
            value = dni, onValueChange = { dni = it },
            label = { Text("DNI") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = nombre, onValueChange = { nombre = it },
            label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = direccion, onValueChange = { direccion = it },
            label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = telefono, onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = transformacion,
            trailingIcon = iconoOjo,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = confirmar,
            onValueChange = { confirmar = it },
            label = { Text("Confirmar contraseña") },
            isError = confirmError != null,
            supportingText = { if (confirmError != null) Text(confirmError) },
            visualTransformation = transformacion,
            trailingIcon = iconoOjo,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val cliente = ClienteDTO(
                    id = 0,
                    dni = dni,
                    nombre = nombre,
                    direccion = direccion,
                    telefono = telefono,
                    email = email,
                    password = password
                )
                onRegister(cliente)
            },
            enabled = formularioValido,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Registrarse") }
    }
}