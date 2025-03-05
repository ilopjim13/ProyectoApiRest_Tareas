package com.example.proyectoapirest_tareas.error.exceptions

/**
 * Excepción personalizada que representa un error de solicitud incorrecta (HTTP 401).
 */
class UnauthorizedException(message:String) :Exception("Not authorized exception (401). $message") {
}