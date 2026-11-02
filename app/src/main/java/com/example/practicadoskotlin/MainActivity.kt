package com.example.practicadoskotlin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practicadoskotlin.ui.login.LoginScreen
import com.example.practicadoskotlin.ui.registro.RegistroScreen
import com.example.practicadoskotlin.ui.theme.PracticaDosKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaDosKotlinTheme {   // el nombre que genere tu Theme.kt
                val navController = rememberNavController()
                NavHost(navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(onGoToRegister = { navController.navigate("register") })
                    }
                    composable("register") {
                        RegistroScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        PracticaDosKotlinTheme {
            Greeting("Android")
        }
    }
}