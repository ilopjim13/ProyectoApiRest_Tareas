package com.example.proyectoapirest_tareas.model

import java.util.Date

data class Tarea(
    val titulo:String,
    val descripcion:String,
    val fecha:Date,
    val estado:Boolean,
    val creador:String
)
