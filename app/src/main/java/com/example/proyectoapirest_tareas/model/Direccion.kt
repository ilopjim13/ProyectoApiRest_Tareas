package com.example.proyectoapirest_tareas.model

/**
 * Data class con los datos de la Dirección
 */
data class Direccion(
    val calle:String,
    val num:String,
    val municipio:String,
    val provincia:String,
    val cp:String
)
