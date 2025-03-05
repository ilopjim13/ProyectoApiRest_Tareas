package com.example.proyectoapirest_tareas.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectoapirest_tareas.R
import com.example.proyectoapirest_tareas.api.Api.getToken
import com.example.proyectoapirest_tareas.error.ErrorDialog
import com.example.proyectoapirest_tareas.navigation.Registro
import com.example.proyectoapirest_tareas.navigation.Tareas
import com.example.proyectoapirest_tareas.viewmodel.TareaViewModel
import com.example.proyectoapirest_tareas.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(modifier: Modifier = Modifier,usuarioViewModel:UsuarioViewModel, tareaViewModel: TareaViewModel, navHostController: NavController) {
    val username by usuarioViewModel.username.collectAsState("")
    val password by usuarioViewModel.pass.collectAsState("")
    val logeado by usuarioViewModel.isLogged.collectAsState(false)
    val error by usuarioViewModel.isError.collectAsState(false)
    val message by usuarioViewModel.messageError.collectAsState("")

    if (error) ErrorDialog(message) {
        usuarioViewModel.closeError()
    }

    Box(Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.fondo),
            contentDescription = "fondo",
            Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {


            Image(
                painter = painterResource(R.drawable.icono),
                contentDescription = "Icono"
            )


            OutlinedTextField(
                value = username,
                onValueChange = { usuarioViewModel.changeLogin(it, password)},
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { usuarioViewModel.changeLogin(username, it) },
                label =  { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        getToken(username, password, usuarioViewModel, tareaViewModel) {
                            usuarioViewModel.openError(it)
                        }
                    } else {
                        usuarioViewModel.openError("Debes rellenar los campos.")
                    }
                }) {
                    Text("Iniciar sesión")
                }
                Button(onClick = {
                    navHostController.navigate(Registro)
                }) {
                    Text("Registrarse")
                }
            }
            LaunchedEffect(logeado) {
                if (logeado) navHostController.navigate(Tareas)
            }
        }
    }


}