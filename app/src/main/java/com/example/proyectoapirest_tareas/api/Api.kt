package com.example.proyectoapirest_tareas.api

import android.util.Log
import com.example.proyectoapirest_tareas.dto.LoginUsuarioDTO
import com.example.proyectoapirest_tareas.dto.TareaAddDTO
import com.example.proyectoapirest_tareas.dto.UsuarioRegisterDTO
import com.example.proyectoapirest_tareas.error.exceptions.BadRequestException
import com.example.proyectoapirest_tareas.error.exceptions.NotFoundException
import com.example.proyectoapirest_tareas.error.exceptions.UnauthorizedException
import com.example.proyectoapirest_tareas.model.Tarea
import com.example.proyectoapirest_tareas.model.Usuario
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

object Api {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    val retrofitService:APIService by lazy {
        getRetrofit().create(APIService::class.java)
    }

    private fun getRetrofit():Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

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

    suspend fun getTareas(token:String, username: String): Response<List<Tarea>> {
        return retrofitService.getTasks("Bearer $token", username)
    }

    suspend fun getAllTareas(token:String): Response<List<Tarea>> {
        return retrofitService.getAllTasks("Bearer $token")
    }

    fun getUsers(token: String, usuarioViewModel: UsuarioViewModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val listaUsuarios = retrofitService.getUsers("Bearer $token")
            if(listaUsuarios.isSuccessful) {
                usuarioViewModel.getUsers(listaUsuarios)
            }
        }
    }

}