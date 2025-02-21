package com.example.proyectoapirest_tareas.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectoapirest_tareas.api.Api.getToken
import com.example.proyectoapirest_tareas.error.ErrorDialog
import com.example.proyectoapirest_tareas.navigation.Registro
import com.example.proyectoapirest_tareas.navigation.Tareas
import com.example.proyectoapirest_tareas.viewmodel.TareaViewModel
import com.example.proyectoapirest_tareas.viewmodel.UsuarioViewModel


@Composable
fun LoginScreen(modifier: Modifier = Modifier,usuarioViewModel:UsuarioViewModel, tareaViewModel: TareaViewModel, navHostController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val logeado by usuarioViewModel.isLogged.collectAsState(false)
    var error by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    if (error) ErrorDialog(message) {
        error = false
        message = ""
    }

    Column(modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it},
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label =  { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    getToken(username, password, usuarioViewModel, tareaViewModel) {
                        error = true
                        message = it
                    }
                }
            }) {
                Text("LOGEARSER")
            }
            Button(onClick = {
                navHostController.navigate(Registro)
            }) {
                Text("REGISTRARSE")
            }
        }
        LaunchedEffect(logeado) {
            if (logeado) navHostController.navigate(Tareas)
        }
    }
}