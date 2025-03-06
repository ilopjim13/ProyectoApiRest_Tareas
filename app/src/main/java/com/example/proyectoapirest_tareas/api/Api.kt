package com.example.proyectoapirest_tareas.api

import android.util.Log
import com.example.proyectoapirest_tareas.dto.LoginUsuarioDTO
import com.example.proyectoapirest_tareas.dto.TareaAddDTO
import com.example.proyectoapirest_tareas.dto.UsuarioRegisterDTO
import com.example.proyectoapirest_tareas.error.exceptions.BadRequestException
import com.example.proyectoapirest_tareas.error.exceptions.NotFoundException
import com.example.proyectoapirest_tareas.error.exceptions.UnauthorizedException
import com.example.proyectoapirest_tareas.model.Tarea
import com.example.proyectoapirest_tareas.service.APIService
import com.example.proyectoapirest_tareas.viewmodel.TareaViewModel
import com.example.proyectoapirest_tareas.viewmodel.UsuarioViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

/**
 * Objeto que gestiona la comunicación con la API mediante Retrofit. Proporciona métodos para
 * la autenticación, registro de usuarios y gestión de tareas.
 */
object Api {

    /** URL base del servidor para realizar las peticiones. */
    private const val BASE_URL = "https://apirest-tareas.onrender.com/"

    /** Instancia de Retrofit configurada con la base URL y el conversor Gson. */
    val retrofitService:APIService by lazy {
        getRetrofit().create(APIService::class.java)
    }

    /** Configura y devuelve una instancia de Retrofit. */
    private fun getRetrofit():Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Realiza la autenticación de un usuario, obteniendo un token si las credenciales son correctas.
     * En caso de éxito, almacena el token y actualiza el estado del usuario en la aplicación.
     */
    fun getToken(username:String, pass:String, usuarioViewModel: UsuarioViewModel, tareaViewModel: TareaViewModel, onDismiss:(String)-> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val auth = retrofitService.login(LoginUsuarioDTO(username, pass))
                if(auth.isSuccessful && auth.errorBody() == null) {
                    val token = auth.body()?.token
                    if (token != null) {
                        Log.d("Login", "Token recibido: $token")
                        withContext(Dispatchers.Main) {
                            usuarioViewModel.isLoggin(true)
                        }
                        tareaViewModel.takeToken(token)
                        usuarioViewModel.getUser(token)
                    }
                }  else {
                    val error = auth.errorBody()?.string()
                    Log.e("ErrorLogin", "Error en login: $error")
                    throw UnauthorizedException("Error en login: $error")
                }
            } catch (e:Exception) {
                withContext(Dispatchers.Main) {
                    onDismiss(e.message.toString())
                }
            }
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * En caso de éxito, muestra un mensaje en el log con la confirmación del registro.
     */
    fun onRegisterClick(usuario: UsuarioRegisterDTO, onDismiss:(String)-> Unit):Boolean {
        var registrado = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val usuarioBD = retrofitService.register(usuario)
                if (usuarioBD.isSuccessful && usuarioBD.errorBody() == null) {
                    Log.d("REGISTER", "El usuario ${usuario.username} ha sido registrado")
                    withContext(Dispatchers.Main) {
                        registrado = true
                    }
                } else {
                    val error = usuarioBD.errorBody()?.string()
                    Log.e("ErrorRegistro", "ERROR de Registro. $error")
                    throw NotFoundException("Error de Registro. $error")
                }
            } catch (e:Exception) {
                withContext(Dispatchers.Main) {
                    onDismiss(e.message.toString())
                }
            }
        }
        return registrado
    }

    /**
     * Sincroniza el estado de las tareas no guardadas con el servidor.
     * Actualiza el estado de las tareas modificadas y elimina aquellas que ya no están en la lista.
     */
    fun save(tareasSinGuardar:List<Tarea>, tareas:List<Tarea>, token:String) {
        CoroutineScope(Dispatchers.IO).launch {
            tareasSinGuardar.forEach {
                val tarea = tareas.find { t -> it.titulo == t.titulo && it.creador == t.creador }
                if (it.estado != tarea?.estado) {
                    retrofitService.updateState("Bearer $token", it.titulo, it.creador)
                    it.estado = tarea?.estado == true
                }

                if (it !in tareas) retrofitService.deleteTask("Bearer $token", it.titulo, it.creador)

            }
        }
    }

    /**
     * Agrega una nueva tarea al usuario autenticado si no existe una con el mismo título y creador.
     * En caso de error, maneja las excepciones adecuadas según la respuesta del servidor.
     */
    fun addTask(titulo:String, desc:String, creador:String, tareas: MutableList<Tarea>, token: String, onDismiss:(String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val tarea = TareaAddDTO(titulo, desc)
            val existe = tareas.find { titulo == it.titulo && creador == it.creador }
            if (existe == null){
                try {
                    val t = retrofitService.addTask("Bearer ${token}", creador, tarea)
                    if (t.isSuccessful && t.errorBody() == null) {
                        Log.e("TAREAAAAAAA", "Tarea ${t.body()?.titulo}")
                        val tareaNueva = Tarea(titulo, desc, Date(), false, creador)
                        tareas.add(tareaNueva)
                    } else {
                        val error = t.errorBody()?.string()
                        Log.e("ErrorDeTarea", "ERROR al agregar la tarea. $error")
                        if (error != null) {
                            if(error.contains("400")) throw BadRequestException(error)
                            else throw NotFoundException(error)
                        }
                    }
                } catch (e:Exception) {
                    withContext(Dispatchers.Main) {
                        onDismiss(e.message.toString())
                    }
                }
            }
        }
    }

    /**
     * Obtiene la lista de tareas asociadas a un usuario autenticado.
     */
    suspend fun getTareas(token:String, username: String): Response<List<Tarea>> {
        return retrofitService.getTasks("Bearer $token", username)
    }

    /**
     * Obtiene la lista de todas las tareas disponibles en el sistema.
     */
    suspend fun getAllTareas(token:String): Response<List<Tarea>> {
        return retrofitService.getAllTasks("Bearer $token")
    }

    /**
     * Obtiene la lista de usuarios registrados y actualiza el estado en el ViewModel correspondiente.
     */
    fun getUsers(token: String, usuarioViewModel: UsuarioViewModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val listaUsuarios = retrofitService.getUsers("Bearer $token")
            if(listaUsuarios.isSuccessful) {
                usuarioViewModel.getUsers(listaUsuarios)
            }
        }
    }

}