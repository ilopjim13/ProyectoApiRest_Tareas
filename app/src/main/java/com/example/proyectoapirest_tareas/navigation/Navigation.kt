package com.example.proyectoapirest_tareas.navigation

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

/**
 * App Tareas clase que se encarga de la navegación de las pantallas
 */
@Composable
fun AppTareas(modifier:Modifier = Modifier, navigationController: NavHostController) {

    // Inicializamos los viewModel y se los enchufamos a las pantallas
    val tareaViewModel by remember { mutableStateOf(TareaViewModel()) }
    val usuarioViewModel by remember { mutableStateOf(UsuarioViewModel(tareaViewModel)) }

    NavHost( // Creamos el navHost que nos llevará a las pantallas
        navController = navigationController,
        startDestination = Login
    ) {
        composable<Login> {
            LoginScreen(modifier, usuarioViewModel, tareaViewModel, navigationController)
        }
        composable<Registro> {
            RegistroScreen(navigationController, usuarioViewModel)
        }
        composable<Tareas> {
            TareasScreen(usuarioViewModel, tareaViewModel, navigationController)
        }
    }
}