package com.example.proyectoapirest_tareas.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Lista de tareas", fontSize = 30.sp)

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(tareas.size) {
                ListItem(tareas[it])
            }
        }
    }
}

@Composable
fun ListItem(tarea:Tarea) {
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
                    onCheckedChange = { /* Aquí maneja el cambio de estado del checkbox */ }
                )

                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar tarea")
                }
            }
        }
    }
}


