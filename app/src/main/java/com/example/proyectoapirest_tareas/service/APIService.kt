package com.example.proyectoapirest_tareas.service

import com.example.proyectoapirest_tareas.model.AuthResponse
import com.example.proyectoapirest_tareas.model.Tarea
import com.example.proyectoapirest_tareas.model.Usuario
import com.example.proyectoapirest_tareas.dto.LoginUsuarioDTO
import com.example.proyectoapirest_tareas.dto.TareaAddDTO
import com.example.proyectoapirest_tareas.dto.UsuarioRegisterDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    @POST("usuarios/login")
    suspend fun login(@Body user: LoginUsuarioDTO) :Response<AuthResponse>

    @POST("usuarios/register")
    suspend fun register(@Body user: UsuarioRegisterDTO) :Response<Usuario>

    @GET("usuarios/usuario")
    suspend fun getUser(@Header("Authorization") authResponse: String) :Response<Usuario>

    @GET("tarea/mostrarTodas")
    suspend fun getAllTasks(@Header("Authorization") authResponse: String) :Response<List<Tarea>>

    @GET("tarea/mostrar/{username}")
    suspend fun getTasks(@Header("Authorization") authResponse: String, @Path("username") username:String) :Response<List<Tarea>>

    @POST("/tarea/agregarTarea/{username}")
    suspend fun addTask(@Header("Authorization") authResponse: String, @Path("username") username:String, @Body tareaAddDTO:TareaAddDTO) :Response<Tarea>

    @POST("/tarea/actualizarEstado/{titulo}/{username}")
    suspend fun updateState(@Header("Authorization") authResponse: String, @Path("titulo") titulo:String, @Path("username") username:String)

    @DELETE("/tarea/eliminarTarea/{titulo}/{username}")
    suspend fun deleteTask(@Header("Authorization") authResponse: String, @Path("titulo") titulo:String, @Path("username") username:String)

}