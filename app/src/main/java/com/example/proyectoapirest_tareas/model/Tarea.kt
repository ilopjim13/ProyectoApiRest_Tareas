package com.example.proyectoapirest_tareas.model

import java.util.Date

/**
 * Data class con los datos de la tarea
 */
data class Tarea(
    val titulo:String,
    val descripcion:String,
    val fecha:Date,
    var estado:Boolean,
    val creador:String
)
