package com.example.practicadoskotlin.ui.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pizzeria.modelo.ClienteDTO

@Composable
fun RegistroScreen(onRegister: (ClienteDTO) -> Unit = {}) {
    var dni by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var direccio by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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
            value = direccio, onValueChange = { direccio = it },
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
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val cliente = ClienteDTO(
                    id = 0,
                    dni = dni,
                    nombre = nombre,
                    direccio = direccio,
                    telefono = telefono,
                    email = email,
                    password = password
                )
                onRegister(cliente)
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Registrarse") }
    }
}