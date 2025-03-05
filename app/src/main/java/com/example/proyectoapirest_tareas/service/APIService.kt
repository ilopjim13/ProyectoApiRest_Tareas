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
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interfaz que define los endpoints de la API para la gestión de usuarios y tareas.
 */
interface APIService {

    /**
     * Realiza el inicio de sesión de un usuario enviando sus credenciales.
     */
    @POST("usuarios/login")
    suspend fun login(@Body user: LoginUsuarioDTO) :Response<AuthResponse>

    /**
     * Registra un nuevo usuario en el sistema.
     */
    @POST("usuarios/register")
    suspend fun register(@Body user: UsuarioRegisterDTO) :Response<Usuario>

    /**
     * Obtiene la información del usuario autenticado.
     */
    @GET("usuarios/usuario")
    suspend fun getUser(@Header("Authorization") authResponse: String) :Response<Usuario>

    /**
     * Obtiene la lista de todos los usuarios registrados en el sistema.
     */
    @GET("usuarios/usuarios")
    suspend fun getUsers(@Header("Authorization") authResponse: String) :Response<List<Usuario>>

    /**
     * Obtiene todas las tareas disponibles en el sistema.
     */
    @GET("tarea/mostrarTodas")
    suspend fun getAllTasks(@Header("Authorization") authResponse: String) :Response<List<Tarea>>

    /**
     * Obtiene la lista de tareas asociadas a un usuario específico.
     */
    @GET("tarea/mostrar/{username}")
    suspend fun getTasks(@Header("Authorization") authResponse: String, @Path("username") username:String) :Response<List<Tarea>>

    /**
     * Agrega una nueva tarea para un usuario determinado.
     */
    @POST("/tarea/agregarTarea/{username}")
    suspend fun addTask(@Header("Authorization") authResponse: String, @Path("username") username:String, @Body tareaAddDTO:TareaAddDTO) :Response<Tarea>

    /**
     * Actualiza el estado de una tarea específica de un usuario.
     */
    @POST("/tarea/actualizarEstado/{titulo}/{username}")
    suspend fun updateState(@Header("Authorization") authResponse: String, @Path("titulo") titulo:String, @Path("username") username:String)

    /**
     * Elimina una tarea específica de un usuario.
     */
    @DELETE("/tarea/eliminarTarea/{titulo}/{username}")
    suspend fun deleteTask(@Header("Authorization") authResponse: String, @Path("titulo") titulo:String, @Path("username") username:String)

}