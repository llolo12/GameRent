package com.example.gamerent.ui.legacy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.gamerent.ui.components.AddEditGameDialog
import com.example.gamerent.ui.components.ConfirmDeleteDialog
import com.example.gamerent.ui.components.ErrorDialog
import com.example.gamerent.ui.components.GameDetailDialog
import com.example.gamerent.viewmodel.GameViewModel

@Composable
fun GameRentDialogHost(viewModel: GameViewModel) {
    val showForm by viewModel.mostrarFormulario.collectAsState()
    val editingGame by viewModel.juegoEditar.collectAsState()
    val deletingGame by viewModel.juegoEliminar.collectAsState()
    val detailGame by viewModel.juegoDetalle.collectAsState()
    val errorMessage by viewModel.mensajeError.collectAsState()

    if (showForm) {
        AddEditGameDialog(
            juegoEditar = editingGame,
            onDismiss = viewModel::cerrarFormulario,
            onSave = { name, console, genre, available, mediaUri, mediaType, price ->
                viewModel.guardarVideoGame(name, console, genre, available, mediaUri, mediaType, price)
            },
            onError = viewModel::mostrarError
        )
    }
    deletingGame?.let { game ->
        ConfirmDeleteDialog(
            videojuego = game,
            onConfirm = viewModel::confirmarEliminacion,
            onDismiss = viewModel::cancelarEliminacion
        )
    }
    detailGame?.let { game ->
        GameDetailDialog(videojuego = game, onDismiss = viewModel::cerrarDetalle)
    }
    errorMessage?.let { message ->
        ErrorDialog(mensaje = message, onDismiss = viewModel::limpiarError)
    }
}
