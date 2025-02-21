package com.example.proyectoapirest_tareas.api

import android.util.Log
import com.example.proyectoapirest_tareas.dto.LoginUsuarioDTO
import com.example.proyectoapirest_tareas.dto.UsuarioRegisterDTO
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
                if(auth.isSuccessful) {
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


    fun onRegisterClick(usuario: UsuarioRegisterDTO, onDismiss:(String)-> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val usuarioBD = retrofitService.register(usuario)
                if (usuarioBD.isSuccessful) {
                    Log.e("REGISTER", "COMPLETADOOOOOOOO")
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

}