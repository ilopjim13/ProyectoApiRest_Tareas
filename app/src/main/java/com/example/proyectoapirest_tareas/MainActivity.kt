package com.example.proyectoapirest_tareas

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.proyectoapirest_tareas.api.Api.retrofitService
import com.example.proyectoapirest_tareas.model.Tarea
import com.example.proyectoapirest_tareas.dto.LoginUsuarioDTO
import com.example.proyectoapirest_tareas.navigation.AppTareas
import com.example.proyectoapirest_tareas.ui.theme.ProyectoApiRest_TareasTheme
import com.example.proyectoapirest_tareas.viewmodel.TareaViewModel
import com.example.proyectoapirest_tareas.viewmodel.UsuarioViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var allTareas = emptyList<Tarea>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoApiRest_TareasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppTareas(modifier = Modifier.padding(innerPadding), rememberNavController(),this )
                    //LoginScreen(modifier = Modifier.padding(innerPadding),this)
                }
            }
        }
    }

    private fun showError() {
        runOnUiThread {
            Toast.makeText(this, "Error al hacer la llamada", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showSucces() {
        runOnUiThread {
            Toast.makeText(this, "Petición correcta", Toast.LENGTH_SHORT).show()
        }
    }
}


fun getToken(username:String, pass:String, context: Context,usuarioViewModel:UsuarioViewModel,tareaViewModel: TareaViewModel, onLog:() -> Unit ) {
    CoroutineScope(Dispatchers.IO).launch {
        val auth = retrofitService.login(LoginUsuarioDTO(username, pass))
        if(auth.isSuccessful) {
            val token = auth.body()?.token
            if (token != null) {
                saveToken(token, context) // Guardar el token de forma segura
                Log.d("Login", "Token recibido: $token")
                onLog()
                tareaViewModel.takeToken(token)
                usuarioViewModel.getUser(token)
            } else {
                Log.e("Login", "El token es nulo")
            }
        }  else {
            Log.e("Login", "Error en login: ${auth.errorBody()?.string()}")
        }
    }
}

private fun saveToken(token: String, context: Context) {
    val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("TOKEN_KEY", token)
        apply()
    }
}