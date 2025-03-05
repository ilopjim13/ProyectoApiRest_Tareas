package com.example.proyectoapirest_tareas.error.exceptions

/**
 * Excepción personalizada que representa un error de solicitud incorrecta (HTTP 404).
 */
class NotFoundException(message:String) :Exception("Not found exception (404). $message") {
}