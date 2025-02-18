package com.example.proyectoapirest_tareas.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectoapirest_tareas.screen.LoginScreen
import com.example.proyectoapirest_tareas.screen.RegistroScreen
import com.example.proyectoapirest_tareas.screen.TareasScreen
import com.example.proyectoapirest_tareas.viewmodel.TareaViewModel
import com.example.proyectoapirest_tareas.viewmodel.UsuarioViewModel

@Composable
fun AppTareas(modifier:Modifier = Modifier, navigationController: NavHostController,context: Context) {

    val tareaViewModel by remember { mutableStateOf(TareaViewModel()) }
    val usuarioViewModel by remember { mutableStateOf(UsuarioViewModel(tareaViewModel)) }

    NavHost(
        navController = navigationController,
        startDestination = Login
    ) {
        composable<Login> {
            LoginScreen(modifier, context, usuarioViewModel, tareaViewModel, navigationController)
        }
        composable<Registro> {
            RegistroScreen(navigationController)
        }
        composable<Tareas> {
            TareasScreen(usuarioViewModel, tareaViewModel)
        }
    }
}
//startDestination = if (usuarioActivo == null) "Portada" else "Menu"