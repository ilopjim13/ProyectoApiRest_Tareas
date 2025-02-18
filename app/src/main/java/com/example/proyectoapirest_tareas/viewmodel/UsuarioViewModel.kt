package com.example.proyectoapirest_tareas.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.proyectoapirest_tareas.api.Api.retrofitService
import com.example.proyectoapirest_tareas.model.Usuario

class UsuarioViewModel(private val tareaViewModel: TareaViewModel) {

    var usuario: Usuario = Usuario("", "", "")

    private val _token = mutableStateOf("")
    val token:State<String> = _token

    fun takeToken(token:String) {
        _token.value = token
    }

    suspend fun getUser(token:String) {
        val user = retrofitService.getUser("Bearer $token")
        if (user.isSuccessful) {
            usuario = user.body() ?: Usuario("", "", "")
            tareaViewModel.loadList(usuario, token)
        }
    }

}