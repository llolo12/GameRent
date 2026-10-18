package com.example.gamerent.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.gamerent.model.Videojuego

@Composable
fun ConfirmDeleteDialog(
    videojuego: Videojuego,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("¿Eliminar videojuego?") },
        text = { Text("Se eliminará '${videojuego.nombre}' del catálogo de GameRent.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Eliminar", color = Color(0xFFFF8D8D))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
