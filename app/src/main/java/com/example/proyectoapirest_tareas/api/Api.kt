package com.example.proyectoapirest_tareas.api

import com.example.proyectoapirest_tareas.service.APIService
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

}