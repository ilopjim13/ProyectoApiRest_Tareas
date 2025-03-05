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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectoapirest_tareas.R
import com.example.proyectoapirest_tareas.api.Api.onRegisterClick
import com.example.proyectoapirest_tareas.dto.UsuarioRegisterDTO
import com.example.proyectoapirest_tareas.error.ErrorDialog
import com.example.proyectoapirest_tareas.model.Direccion
import com.example.proyectoapirest_tareas.viewmodel.UsuarioViewModel

/**
 * Pantalla de registro donde nos permite registrarnos o ir a iniciar sesión
 */
@Composable
fun RegistroScreen(navController: NavHostController, usuarioViewModel: UsuarioViewModel) {
    // Inicializamos las variables del registro
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepeat by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var num by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var cp by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    // Dialog que aparecerá si hay error con el mensaje del error
    if (error) {
        ErrorDialog("Error de Registro: $message") {
            error = false
            message = ""
        }
    }

    Image(
        painter = painterResource(R.drawable.fondo),
        contentDescription = "fondo",
        Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Ingresamos los campos para los datos
        OutlinedTextField(value = username, onValueChange = { username = it }, singleLine = true, label = { Text("Usuario") })
        OutlinedTextField(value = email, onValueChange = { email = it }, singleLine = true, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, singleLine = true, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(value = passwordRepeat, onValueChange = { passwordRepeat = it }, singleLine = true, label = { Text("Repetir Contraseña") }, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(value = calle, onValueChange = { calle = it }, singleLine = true, label = { Text("Calle") })
        OutlinedTextField(value = num, onValueChange = { num = it }, singleLine = true, label = { Text("Número") })
        OutlinedTextField(value = municipio, onValueChange = { municipio = it }, singleLine = true, label = { Text("Municipio") })
        OutlinedTextField(value = provincia, onValueChange = { provincia = it }, singleLine = true, label = { Text("Provincia") })
        OutlinedTextField(value = cp, onValueChange = { cp = it }, singleLine = true, label = { Text("Código Postal") })

        Spacer(modifier = Modifier.height(16.dp)) // Separamos los campos con los botones

        Row( // Fila para los botones
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                // Creamos un usuarioRegisterDTO con la direccíon y el usuario
                val direccion = Direccion(calle, num, municipio, provincia, cp)
                val usuario = UsuarioRegisterDTO(username, email, password, passwordRepeat, "USER", direccion)
                if (usuarioViewModel.checkUser(usuario)) { // Comprobamos que los datos ingresados estén bien
                    if (onRegisterClick(usuario) { // Guardamos el usuario si está bien nos mandará al login
                            error = true
                            message = it
                        }) navController.popBackStack()
                } else { // Si no nos dará un error
                    error = true
                    message = "Error en los campos introducidos. Comprueba que todos los datos estén introducidos, el email sea correcto, y la contraseña sea mayor o igual a 4 dígitos"
                }
            }) {
                Text("Registrarse")
            }
            Button(onClick = { navController.popBackStack() }) {
                Text("Iniciar Sesión") // Botón para volver a iniciar sesión
            }
        }


    }

}

