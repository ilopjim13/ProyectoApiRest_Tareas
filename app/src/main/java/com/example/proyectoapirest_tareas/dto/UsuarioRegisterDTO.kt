package com.example.proyectoapirest_tareas.dto

import com.example.proyectoapirest_tareas.model.Direccion

data class UsuarioRegisterDTO(
    val username:String,
    val email:String,
    val password:String,
    val passwordRepeat:String,
    val rol:String?,
    val direccion: Direccion
)