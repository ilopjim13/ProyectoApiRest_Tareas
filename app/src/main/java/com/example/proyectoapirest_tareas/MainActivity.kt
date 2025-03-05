package com.example.proyectoapirest_tareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.proyectoapirest_tareas.navigation.AppTareas
import com.example.proyectoapirest_tareas.ui.theme.ProyectoApiRest_TareasTheme

/**
 * Clase principal donde ejecutaremos el Navigation
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoApiRest_TareasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppTareas(modifier = Modifier.padding(innerPadding), rememberNavController())
                }
            }
        }
    }
}


