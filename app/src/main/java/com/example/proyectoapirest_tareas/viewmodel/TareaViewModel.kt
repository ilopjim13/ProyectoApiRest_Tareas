package com.example.proyectoapirest_tareas.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.proyectoapirest_tareas.api.Api
import com.example.proyectoapirest_tareas.model.Tarea
import com.example.proyectoapirest_tareas.model.Usuario

/**
 * ViewModel encargado de la gestión de tareas. Maneja la carga, modificación y eliminación
 * de tareas, así como la persistencia de datos en la API.
 */
class TareaViewModel {

    /** Lista de tareas cargadas desde la API o creadas por el usuario. */
    var tareas: SnapshotStateList<Tarea> = mutableStateListOf()
    /** Lista de tareas sin guardar para gestionar cambios pendientes antes de sincronizar con la API. */
    private var tareasSinGuardar: SnapshotStateList<Tarea> = mutableStateListOf()

    private val _token = mutableStateOf("")
    val token: State<String> = _token

    /**
     * Almacena el token de autenticación del usuario.
     */
    fun takeToken(token:String) {
        _token.value = token
    }

    /**
     * Carga la lista de tareas desde la API según el rol del usuario.
     */
    suspend fun loadList(usuario: Usuario, token:String) {

        if(usuario.rol == "USER") { // si el usuario es de tipo "USER", obtiene solo sus propias tareas.
            val tareasBd = Api.getTareas(token, usuario.username)

            if (tareasBd.isSuccessful) {
                tareasBd.body()?.forEach {
                    tareas.add(it)
                    tareasSinGuardar.add(it)
                }
            }
        } else if (usuario.rol == "ADMIN") { // Si el usuario es "ADMIN", obtiene todas las tareas del sistema.
            val tareasBd = Api.getAllTareas(token)

            if (tareasBd.isSuccessful) {
                tareasBd.body()?.forEach {
                    tareas.add(it)
                    tareasSinGuardar.add(it)
                }
            }
        }
    }

    /**
     * Cierra la sesión del usuario, eliminando el token y limpiando las listas de tareas.
     */
    fun closeSession(){
        _token.value = ""
        tareas = mutableStateListOf()
        tareasSinGuardar = mutableStateListOf()
    }

    /**
     * Guarda los cambios realizados en las tareas, sincronizándolos con la API.
     */
    fun save() {
        Api.save(tareasSinGuardar, tareas, token.value)
    }

    /**
     * Agrega una nueva tarea y la almacena en la API si no existe previamente.
     */
    fun addTask(titulo:String, desc:String, creador:String, onDismiss:(String) -> Unit) {
        Api.addTask(titulo,desc,creador, tareas, token.value, onDismiss)
    }

    /**
     * Cambia el estado de una tarea (completada/no completada).
     */
    fun changeState(tarea: Tarea) {
        val index = tareas.indexOfFirst { it.titulo == tarea.titulo }
        if (index != -1) {
            tareas[index] = tareas[index].copy(estado = !tareas[index].estado)
        }
    }

    /**
     * Elimina una tarea de la lista local.
     */
    fun deleteTask(tarea: Tarea) {
        tareas.remove(tarea)
    }

}