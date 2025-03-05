package com.example.proyectoapirest_tareas.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.proyectoapirest_tareas.api.Api
import com.example.proyectoapirest_tareas.model.Tarea
import com.example.proyectoapirest_tareas.model.Usuario

class TareaViewModel {

    val tareas: SnapshotStateList<Tarea> = mutableStateListOf()
    private var tareasSinGuardar: SnapshotStateList<Tarea> = mutableStateListOf()

    private val _token = mutableStateOf("")
    val token: State<String> = _token

    fun takeToken(token:String) {
        _token.value = token
    }

    suspend fun loadList(usuario: Usuario, token:String) {

        if(usuario.rol == "USER") {
            val tareasBd = Api.getTareas(token, usuario.username)

            if (tareasBd.isSuccessful) {
                tareasBd.body()?.forEach {
                    tareas.add(it)
                    tareasSinGuardar.add(it)
                }
            }
        } else if (usuario.rol == "ADMIN") {
            val tareasBd = Api.getAllTareas(token)

            if (tareasBd.isSuccessful) {
                tareasBd.body()?.forEach {
                    tareas.add(it)
                    tareasSinGuardar.add(it)
                }
            }
        }
    }

    fun save() {
        Api.save(tareasSinGuardar, tareas, token.value)
    }

    fun addTask(titulo:String, desc:String, creador:String, onDismiss:(String) -> Unit) {
        Api.addTask(titulo,desc,creador, tareas, token.value, onDismiss)
    }


    fun changeState(tarea: Tarea) {
        val index = tareas.indexOfFirst { it.titulo == tarea.titulo }
        if (index != -1) {
            tareas[index] = tareas[index].copy(estado = !tareas[index].estado)
        }
    }

    fun deleteTask(tarea: Tarea) {
        tareas.remove(tarea)
    }

}