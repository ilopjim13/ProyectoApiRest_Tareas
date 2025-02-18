package com.example.proyectoapirest_tareas.service

import com.example.proyectoapirest_tareas.model.AuthResponse
import com.example.proyectoapirest_tareas.model.Tarea
import com.example.proyectoapirest_tareas.model.Usuario
import com.example.proyectoapirest_tareas.dto.LoginUsuarioDTO
import com.example.proyectoapirest_tareas.dto.UsuarioRegisterDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {

    @POST("usuarios/login")
    suspend fun login(@Body user: LoginUsuarioDTO) :Response<AuthResponse>

    @POST("usuarios/register")
    suspend fun register(@Body user: UsuarioRegisterDTO) :Response<Usuario>

    @GET("tarea/mostrarTodas")
    suspend fun getAllTasks() :Response<List<Tarea>>

    @Headers("Authorization: Bearer ")
    @FormUrlEncoded
    @GET("tarea/mostrar")
    suspend fun getTasks(@Field("username") username:String) :Response<List<Tarea>>
}