package com.example.proyectoapirest_tareas.screen

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectoapirest_tareas.R
import com.example.proyectoapirest_tareas.api.Api
import com.example.proyectoapirest_tareas.error.ErrorDialog
import com.example.proyectoapirest_tareas.model.Tarea
import com.example.proyectoapirest_tareas.navigation.Login
import com.example.proyectoapirest_tareas.viewmodel.TareaViewModel
import com.example.proyectoapirest_tareas.viewmodel.UsuarioViewModel

@Composable
fun TareasScreen(usuarioViewModel: UsuarioViewModel, tareaViewModel: TareaViewModel, navHost: NavHostController) {

    val tareas = remember { tareaViewModel.tareas }
    var agregar by remember { mutableStateOf(false) }
    val error by usuarioViewModel.isError.collectAsState(false)
    val message by usuarioViewModel.messageError.collectAsState("")

    if (error) {
        ErrorDialog(message) {
            usuarioViewModel.closeError()
        }
    }

    if (agregar) {
        AddDialog(onConfirm = { titulo, desc, creador ->
            if (titulo.isNotBlank() && desc.isNotBlank())
                tareaViewModel.addTask(titulo, desc, creador,
                    onDismiss = {
                        usuarioViewModel.openError(it)
                    })
        }, onDismiss = {agregar = false}, usuarioViewModel, tareaViewModel.token.value)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {

        Image(
            painter = painterResource(R.drawable.circulitos),
            contentDescription = "fondo",
            Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp, top = 32.dp, start = 16.dp, end = 16.dp ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                Text(text = "Lista de tareas", fontSize = 30.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End

                )  {
                    IconButton({
                        navHost.navigate(Login)
                        usuarioViewModel.closeSession()
                        tareaViewModel.closeSession()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = ""
                        )
                    }
                }
            }


            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(top = 16.dp, bottom = 16.dp)
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
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 94.dp, end = 32.dp)
            ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Añadir tarea")
        }

        Button(
            onClick = { tareaViewModel.save() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 32.dp, end = 16.dp, start = 16.dp)
        ) {
            Text(text = "Guardar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDialog(onConfirm:(String, String, String)->Unit, onDismiss:()->Unit, usuarioViewModel: UsuarioViewModel, token:String) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var creador by remember { mutableStateOf(usuarioViewModel.usuario.username) }
    var expanded by remember { mutableStateOf(false) }

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
                if(usuarioViewModel.usuario.rol == "ADMIN"){
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = creador,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Rol") },
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar")
                            },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            if (usuarioViewModel.usuarios.isEmpty()) Api.getUsers(token, usuarioViewModel)
                            val usuarios = usuarioViewModel.usuarios
                            usuarios.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.username) },
                                    onClick = {
                                        creador = option.username
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
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


