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

/**
 * ViewModel que gestiona la lógica relacionada con el usuario y la autenticación. Mantiene el
 * estado del usuario, la lista de usuarios registrados y maneja la autenticación.
 * También interactúa con `TareaViewModel` para cargar tareas después de la autenticación.
 */
class UsuarioViewModel(private val tareaViewModel: TareaViewModel) {

    /** Usuario autenticado en la sesión actual. */
    var usuario: Usuario = Usuario("", "", "")

    /** Lista de usuarios registrados en el sistema. */
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

    /**
     * Obtiene los datos del usuario autenticado utilizando su token.
     * Si la respuesta es exitosa, almacena la información del usuario
     * y carga sus tareas asociadas.
     */
    suspend fun getUser(token:String) {
        val user = retrofitService.getUser("Bearer $token")
        if (user.isSuccessful) {
            usuario = user.body() ?: Usuario("", "", "")
            tareaViewModel.loadList(usuario, token)
        }
    }

    /**
     * Almacena la lista de usuarios obtenida del servidor en la variable `usuarios`.
     */
    fun getUsers(listUsuarios: Response<List<Usuario>>) {
        listUsuarios.body()?.forEach {
            usuarios.add(it)
        }
    }

    /**
     * Actualiza los valores de nombre de usuario y contraseña en el estado del ViewModel.
     */
    fun changeLogin(username:String, pass:String) {
        _username.value = username
        _pass.value = pass
    }

    /**
     * Activa el estado de error y almacena el mensaje de error recibido.
     */
    fun openError(error:String) {
        _isError.value = true
        _messageError.value = error
    }

    /**
     * Cierra el estado de error y limpia el mensaje de error almacenado.
     */
    fun closeError() {
        _isError.value = false
        _messageError.value = ""

    }

    /**
     * Cierra la sesión del usuario, restableciendo sus datos y los estados de autenticación.
     */
    fun closeSession(){
        usuario = Usuario("", "","")
        resetLogin()
        _isLogged.value = false
        _isError.value = false
    }

    /**
     * Restablece los valores de nombre de usuario y contraseña en el estado del ViewModel.
     */
    fun resetLogin() {
        _username.value = ""
        _pass.value = ""
    }

    /**
     * Cambia el estado de autenticación del usuario.
     */
    fun isLoggin(bool: Boolean) {
        _isLogged.value = bool
    }

    /**
     * Verifica si los datos ingresados por el usuario en el formulario de registro son válidos.
     * Retorna `true` si la información es correcta y `false` en caso contrario.
     */
    fun checkUser(user: UsuarioRegisterDTO) :Boolean
    {
        if( user.password.isBlank() || user.username.isBlank() || user.email.isBlank() || user.passwordRepeat.isBlank())
            return false

        if (user.direccion.calle.isBlank() || user.direccion.num.isBlank() || user.direccion.municipio.isBlank() || user.direccion.provincia.isBlank() || user.direccion.cp.isBlank())
            return false

        return Patterns.EMAIL_ADDRESS.matcher(user.email).matches() && user.password.length > 3
    }

}