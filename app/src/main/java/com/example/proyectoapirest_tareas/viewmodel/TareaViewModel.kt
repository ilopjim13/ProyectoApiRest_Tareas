package com.example.proyectoapirest_tareas.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.proyectoapirest_tareas.api.Api.retrofitService
import com.example.proyectoapirest_tareas.dto.TareaAddDTO
import com.example.proyectoapirest_tareas.error.exceptions.BadRequestException
import com.example.proyectoapirest_tareas.error.exceptions.NotFoundException
import com.example.proyectoapirest_tareas.model.Tarea
import com.example.proyectoapirest_tareas.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class TareaViewModel {

    val tareas: SnapshotStateList<Tarea> = mutableStateListOf()
    private var tareasSinGuardar: SnapshotStateList<Tarea> = mutableStateListOf()

    private val _token = mutableStateOf("")
    private val token: State<String> = _token

    fun takeToken(token:String) {
        _token.value = token
    }

    suspend fun loadList(usuario: Usuario, token:String) {

        if(usuario.rol == "USER") {
            val tareasBd = retrofitService.getTasks("Bearer $token", usuario.username)

            if (tareasBd.isSuccessful) {
                tareasBd.body()?.forEach {
                    tareas.add(it)
                    tareasSinGuardar.add(it)
                }
            }
        } else if (usuario.rol == "ADMIN") {
            val tareasBd = retrofitService.getAllTasks("Bearer $token")

            if (tareasBd.isSuccessful) {
                tareasBd.body()?.forEach {
                    tareas.add(it)
                    tareasSinGuardar.add(it)
                }
            }
        }
    }

    fun save() {
        CoroutineScope(Dispatchers.IO).launch {
            tareasSinGuardar.forEach {
                val tarea = tareas.find { t -> it.titulo == t.titulo && it.creador == t.creador }
                if (it.estado != tarea?.estado) {
                    retrofitService.updateState("Bearer ${token.value}", it.titulo, it.creador)
                    it.estado = tarea?.estado == true
                }

                if (it !in tareas) {
                    retrofitService.deleteTask("Bearer ${token.value}", it.titulo, it.creador)
                    tareasSinGuardar = tareas
                }

            }
        }
    }

    fun addTask(titulo:String, desc:String, creador:String, onDismiss:(String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val tarea = TareaAddDTO(titulo, desc)
            val existe = tareas.find { titulo == it.titulo && creador == it.creador }
            if (existe == null){
                try {
                    val t = retrofitService.addTask("Bearer ${token.value}", creador, tarea)
                    if (t.isSuccessful && t.errorBody() == null) {
                        Log.e("TAREAAAAAAA", "Tarea ${t.body()?.titulo}")
                        val tareaNueva = Tarea(titulo, desc, Date(), false, creador)
                        tareas.add(tareaNueva)
                    } else {
                        val error = t.errorBody()?.string()
                        Log.e("ErrorDeTarea", "ERROR al agregar la tarea. $error")
                        if (error != null) {
                            if(error.contains("400")) throw BadRequestException(error)
                            else throw NotFoundException(error)
                        }
                    }
                } catch (e:Exception) {
                    withContext(Dispatchers.Main) {
                        onDismiss(e.message.toString())
                    }
                }
            }
        }
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