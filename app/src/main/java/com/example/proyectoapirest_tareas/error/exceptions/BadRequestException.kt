package com.example.proyectoapirest_tareas.error.exceptions

/**
 * Excepción personalizada que representa un error de solicitud incorrecta (HTTP 400).
 */
class BadRequestException(message:String) :Exception("Bad Request Exception. (400). $message") {
}