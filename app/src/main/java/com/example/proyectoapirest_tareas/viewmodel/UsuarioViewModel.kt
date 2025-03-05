package com.example.proyectoapirest_tareas.viewmodel

import android.util.Patterns
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.proyectoapirest_tareas.api.Api.retrofitService
import com.example.proyectoapirest_tareas.dto.UsuarioRegisterDTO
import com.example.proyectoapirest_tareas.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

class UsuarioViewModel(private val tareaViewModel: TareaViewModel) {

    var usuario: Usuario = Usuario("", "", "")

    var usuarios: SnapshotStateList<Usuario> = mutableStateListOf()

    private var _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private var _pass = MutableStateFlow("")
    val pass: StateFlow<String> = _pass

    private var _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private var _messageError = MutableStateFlow("")
    val messageError: StateFlow<String> = _messageError

    private var _isLogged = MutableStateFlow(false)
    val isLogged: StateFlow<Boolean> = _isLogged

    suspend fun getUser(token:String) {
        val user = retrofitService.getUser("Bearer $token")
        if (user.isSuccessful) {
            usuario = user.body() ?: Usuario("", "", "")
            tareaViewModel.loadList(usuario, token)
        }
    }

    fun getUsers(listUsuarios: Response<List<Usuario>>) {
        listUsuarios.body()?.forEach {
            usuarios.add(it)
        }
    }

    fun changeLogin(username:String, pass:String) {
        _username.value = username
        _pass.value = pass
    }

    fun openError(error:String) {
        _isError.value = true
        _messageError.value = error
    }

    fun closeError() {
        _isError.value = false
        _messageError.value = ""

    }

    fun closeSession(){
        usuario = Usuario("", "","")
        _isLogged.value = false
        _isError.value = false
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