package com.example.proyectoapirest_tareas.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectoapirest_tareas.api.Api.onRegisterClick
import com.example.proyectoapirest_tareas.dto.UsuarioRegisterDTO
import com.example.proyectoapirest_tareas.error.ErrorDialog
import com.example.proyectoapirest_tareas.model.Direccion
import com.example.proyectoapirest_tareas.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavHostController, usuarioViewModel: UsuarioViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepeat by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("USER") }
    var calle by remember { mutableStateOf("") }
    var num by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var cp by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("USER", "ADMIN")
    var error by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }


    if(error) {
        ErrorDialog("Error de Registro: $message") {
            error = false
            message = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = username, onValueChange = { username = it }, singleLine = true, label = { Text("Usuario") })
        OutlinedTextField(value = email, onValueChange = { email = it }, singleLine = true, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, singleLine = true, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(value = passwordRepeat, onValueChange = { passwordRepeat = it }, singleLine = true, label = { Text("Repetir Contraseña") }, visualTransformation = PasswordVisualTransformation())

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = rol,
                onValueChange = {},
                readOnly = true,
                label = { Text("Rol") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar")
                },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roles.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            rol = option
                            expanded = false
                        }
                    )
                }
            }
        }
        OutlinedTextField(value = calle, onValueChange = { calle = it }, singleLine = true, label = { Text("Calle") })
        OutlinedTextField(value = num, onValueChange = { num = it }, singleLine = true, label = { Text("Número") })
        OutlinedTextField(value = municipio, onValueChange = { municipio = it }, singleLine = true, label = { Text("Municipio") })
        OutlinedTextField(value = provincia, onValueChange = { provincia = it }, singleLine = true, label = { Text("Provincia") })
        OutlinedTextField(value = cp, onValueChange = { cp = it }, singleLine = true, label = { Text("Código Postal") })

        Spacer(modifier = Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                val direccion = Direccion(calle, num, municipio, provincia, cp)
                val usuario = UsuarioRegisterDTO(username, email, password, passwordRepeat, rol, direccion)
                if (usuarioViewModel.checkUser(usuario)) {
                    if (onRegisterClick(usuario) {
                        error = true
                        message = it
                    })navController.popBackStack()


                }
                else {
                    error = true
                    message = "Error en los campos introducidos. Comprueba que todos los datos estén introducidos, el email sea correcto, y la contraseña sea mayor o igual a 4 dígitos"
                }
            }) {
                Text("Registrarse")
            }
            Button(onClick = {navController.popBackStack()}) {
                Text("Iniciar Sesión")
            }
        }


    }
}

