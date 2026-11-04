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
import com.example.compose.AppTheme
import com.example.practicadoskotlin.ui.home.HomeScreen
import com.example.practicadoskotlin.ui.login.LoginScreen
import com.example.practicadoskotlin.ui.registro.RegistroScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme(
            ) {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "home") {
                    composable("login") {
                        LoginScreen(onGoToRegister = { navController.navigate("register") })
                    }
                    composable("register") {
                        RegistroScreen()
                    }
                    composable("home") { HomeScreen() }
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
        AppTheme() {
            Greeting("Android")
        }
    }
}