package com.example.proyectoapirest_tareas.error.exceptions

class BadRequestException(message:String) :Exception("Bad Request Exception. (400). $message") {
}