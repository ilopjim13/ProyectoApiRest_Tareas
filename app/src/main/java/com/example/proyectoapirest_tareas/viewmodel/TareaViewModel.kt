package com.example.proyectoapirest_tareas.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.proyectoapirest_tareas.api.Api.retrofitService
import com.example.proyectoapirest_tareas.model.Tarea
import com.example.proyectoapirest_tareas.model.Usuario

class TareaViewModel {

    val tareas: SnapshotStateList<Tarea> = mutableStateListOf()

    suspend fun loadList(usuario: Usuario, token:String) {

        if(usuario.rol == "USER") {
            val tareasBd = retrofitService.getTasks("Bearer $token", usuario.username)

            if (tareasBd.isSuccessful) {
                tareasBd.body()?.forEach {
                    tareas.add(it)
                }
            }
        } else if (usuario.rol == "ADMIN") {
            val tareasBd = retrofitService.getAllTasks("Bearer $token")

            if (tareasBd.isSuccessful) {
                tareasBd.body()?.forEach {
                    tareas.add(it)
                }
            }
        }



    }

}