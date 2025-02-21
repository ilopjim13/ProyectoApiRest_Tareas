package com.example.proyectoapirest_tareas.error.exceptions

class UnauthorizedException(message:String) :Exception("Not authorized exception (401). $message") {
}