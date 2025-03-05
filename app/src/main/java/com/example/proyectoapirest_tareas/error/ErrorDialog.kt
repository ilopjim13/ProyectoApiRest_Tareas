package com.example.proyectoapirest_tareas.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

/**
 * Muestra un dialog de error con un mensaje y un botón de confirmación.
 */
@Composable
fun ErrorDialog(message:String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { // Encabezado del diálogo con el título "Error" y un botón para cerrar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Error", fontWeight = FontWeight.Bold) // Texto en negrita para resaltar el título
                // Botón de cierre con un icono "X"
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                }
            }
        },
        text = { // Cuerpo del diálogo donde se muestra el mensaje de error
            Text(text = message)
        },
        confirmButton = { // Botón de confirmación que cierra el diálogo
            Button(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}