package com.example.proyectoapirest_tareas.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.proyectoapirest_tareas.api.Api.retrofitService
import com.example.proyectoapirest_tareas.dto.UsuarioRegisterDTO
import com.example.proyectoapirest_tareas.model.Direccion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit

@Composable
fun RegistroScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepeat by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var num by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var cp by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(value = passwordRepeat, onValueChange = { passwordRepeat = it }, label = { Text("Repetir Contraseña") }, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(value = rol, onValueChange = { rol = it }, label = { Text("Rol") })
        OutlinedTextField(value = calle, onValueChange = { calle = it }, label = { Text("Calle") })
        OutlinedTextField(value = num, onValueChange = { num = it }, label = { Text("Número") })
        OutlinedTextField(value = municipio, onValueChange = { municipio = it }, label = { Text("Municipio") })
        OutlinedTextField(value = provincia, onValueChange = { provincia = it }, label = { Text("Provincia") })
        OutlinedTextField(value = cp, onValueChange = { cp = it }, label = { Text("Código Postal") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val direccion = Direccion(calle, num, municipio, provincia, cp)
            val usuario = UsuarioRegisterDTO(username, email, password, passwordRepeat, rol, direccion)
            onRegisterClick(usuario)
        }) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {navController.popBackStack()}) {
            Text("Iniciar Sesión")
        }
    }
}

fun onRegisterClick(usuario: UsuarioRegisterDTO) {
    CoroutineScope(Dispatchers.IO).launch {
        val usuarioBD = retrofitService.register(usuario)
        if (usuarioBD.isSuccessful) {
            Log.e("REGISTER", "COMPLETADOOOOOOOO")
        }
    }
}
