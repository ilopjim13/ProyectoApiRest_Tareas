package com.example.proyectoapirest_tareas.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectoapirest_tareas.screen.LoginScreen
import com.example.proyectoapirest_tareas.screen.RegistroScreen

@Composable
fun AppTareas(modifier:Modifier = Modifier, navigationController: NavHostController,context: Context) {

    NavHost(
        navController = navigationController,
        startDestination = Login
    ) {
        composable<Login> {
            LoginScreen(modifier, context, navigationController)
        }
        composable<Registro> {
            RegistroScreen(navigationController)
        }
    }
}
//startDestination = if (usuarioActivo == null) "Portada" else "Menu"