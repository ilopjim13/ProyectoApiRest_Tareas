package com.example.proyectoapirest_tareas

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {

    @POST("/login")
    suspend fun login(@Body user:Usuario) :Response<Usuario>
}