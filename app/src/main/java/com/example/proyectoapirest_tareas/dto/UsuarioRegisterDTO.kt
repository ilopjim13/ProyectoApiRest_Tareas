package com.example.proyectoapirest_tareas.dto

import com.example.proyectoapirest_tareas.model.Direccion

/**
 * Data class que representa la información necesaria para registrar un nuevo usuario.
 */
data class UsuarioRegisterDTO(
    val username:String,
    val email:String,
    val password:String,
    val passwordRepeat:String,
    val rol:String?,
    val direccion: Direccion
)