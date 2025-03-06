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

/**
 * Pantalla del login, que nos permite logearnos o ir a registrarnos
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    usuarioViewModel: UsuarioViewModel,
    tareaViewModel: TareaViewModel,
    navHostController: NavController
) {
    // Cargamos la variables del viewModel a la pantalla
    val username by usuarioViewModel.username.collectAsState("")
    val password by usuarioViewModel.pass.collectAsState("")
    val logeado by usuarioViewModel.isLogged.collectAsState(false)
    val error by usuarioViewModel.isError.collectAsState(false)
    val message by usuarioViewModel.messageError.collectAsState("")

    // Dialog que aparecerá si hay error con el mensaje del error
    if (error) ErrorDialog(message) {
        usuarioViewModel.closeError()
    }



    Image( // Imagen de fondo del login
        painter = painterResource(R.drawable.fondo),
        contentDescription = "fondo",
        Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    // Creamos el column donde iran el icono y los componentes para iniciar sesión o el botón para ir a registrarse
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image( // Icono de la aplicación
            painter = painterResource(R.drawable.icono),
            contentDescription = "Icono"
        )

        PedirLogin(username, password, usuarioViewModel)

        Spacer(modifier = Modifier.height(24.dp))

        Botones(username, password, usuarioViewModel, tareaViewModel, navHostController)

        // Si estamos logeados pasaremos a la pantalla de Tareas
        LaunchedEffect(logeado) {
            if (logeado) navHostController.navigate(Tareas)
        }
    }

}

/**
 * Función que permite pedir los datos del login
 */
@Composable
fun PedirLogin(username: String, password: String, usuarioViewModel: UsuarioViewModel) {
    // TextField para el username
    OutlinedTextField(
        value = username,
        onValueChange = { usuarioViewModel.changeLogin(it, password) },
        label = { Text("Usuario") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
    Spacer(modifier = Modifier.height(16.dp)) // Separador entre ambos campos
    // TextField para la password
    OutlinedTextField(
        value = password,
        onValueChange = { usuarioViewModel.changeLogin(username, it) },
        label = { Text("Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

/**
 * Función que pone los botones y permite acceder a la app o ir a registrarse
 */
@Composable
fun Botones(
    username: String,
    password: String,
    usuarioViewModel: UsuarioViewModel,
    tareaViewModel: TareaViewModel,
    navHostController: NavController
) {
    // Fila para poner los dos botones
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = {
            // Si los campos están rellenos se comprueba
            if (username.isNotBlank() && password.isNotBlank()) {
                // Guarda el token y salta un error si ha ocurrido algo en la api
                getToken(username, password, usuarioViewModel, tareaViewModel) {
                    usuarioViewModel.openError(it)
                }
            } else { // Si no dará un error
                usuarioViewModel.openError("Debes rellenar los campos.")
            }
        }) {
            Text("Iniciar sesión")
        }
        Button(onClick = {
            navHostController.navigate(Registro) // Navega hasta la pantalla Registro
            usuarioViewModel.resetLogin() // Reseteamos las variables del login
        }) {
            Text("Registrarse")
        }
    }
}