package com.example.proyectoapirest_tareas.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoapirest_tareas.model.Tarea
import com.example.proyectoapirest_tareas.viewmodel.TareaViewModel
import com.example.proyectoapirest_tareas.viewmodel.UsuarioViewModel

@Composable
fun TareasScreen(usuarioViewModel: UsuarioViewModel, tareaViewModel: TareaViewModel) {

    val tareas = remember { tareaViewModel.tareas }
    var agregar by remember { mutableStateOf(false) }

    if (agregar) {
        AddDialog(onConfirm = { titulo, desc, creador ->
            if (titulo.isNotBlank() && desc.isNotBlank())
                tareaViewModel.addTask(titulo, desc, creador)
        }, onDismiss = {agregar = false}, usuarioViewModel)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp, top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Lista de tareas", fontSize = 30.sp)

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(tareas.size) { index ->
                    ListItem(tareas[index], tareaViewModel)
                }
            }
        }

        FloatingActionButton(
            onClick = { agregar = true},
            containerColor = Color.LightGray,
            contentColor = Color.Black,
            shape = CircleShape,
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 78.dp, end = 16.dp)
            ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Añadir tarea")
        }

        Button(
            onClick = { tareaViewModel.save() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Guardar")
        }
    }
}

@Composable
fun AddDialog(onConfirm:(String, String, String)->Unit, onDismiss:()->Unit, usuarioViewModel: UsuarioViewModel) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var creador by remember { mutableStateOf(usuarioViewModel.usuario.username) }

    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "Nueva Tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = creador,
                    onValueChange = { creador = it },
                    label = { Text("Creador") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(titulo, descripcion, creador)
                onDismiss()
            }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun ListItem(tarea:Tarea, tareaViewModel: TareaViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = tarea.titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "Creador: ${tarea.creador}",
                    fontSize = 8.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = tarea.descripcion,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Checkbox(
                    checked = tarea.estado,
                    onCheckedChange = {
                        tareaViewModel.changeState(tarea)
                    }
                )

                IconButton(onClick = {tareaViewModel.deleteTask(tarea)}) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar tarea")
                }
            }
        }
    }
}


