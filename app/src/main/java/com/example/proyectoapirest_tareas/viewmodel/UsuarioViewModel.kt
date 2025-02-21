package com.example.proyectoapirest_tareas.viewmodel

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.proyectoapirest_tareas.api.Api.retrofitService
import com.example.proyectoapirest_tareas.dto.UsuarioRegisterDTO
import com.example.proyectoapirest_tareas.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UsuarioViewModel(private val tareaViewModel: TareaViewModel) {

    var usuario: Usuario = Usuario("", "", "")

    private var _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError

    private var _isLogged = MutableStateFlow(false)
    val isLogged: StateFlow<Boolean> = _isLogged

    suspend fun getUser(token:String) {
        val user = retrofitService.getUser("Bearer $token")
        if (user.isSuccessful) {
            usuario = user.body() ?: Usuario("", "", "")
            tareaViewModel.loadList(usuario, token)
        }
    }

    fun showError(bool: Boolean) {
        _isError.value = bool
    }

    fun isLoggin(bool: Boolean) {
        _isLogged.value = bool
    }

    fun checkUser(user: UsuarioRegisterDTO) :Boolean
    {
        if( user.password.isBlank() || user.username.isBlank() || user.email.isBlank() || user.passwordRepeat.isBlank())
            return false

        if (user.direccion.calle.isBlank() || user.direccion.num.isBlank() || user.direccion.municipio.isBlank() || user.direccion.provincia.isBlank() || user.direccion.cp.isBlank())
            return false

        return Patterns.EMAIL_ADDRESS.matcher(user.email).matches() && user.password.length > 3
    }

}