package com.example.gamerent.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.gamerent.ui.components.AddEditGameDialog
import com.example.gamerent.ui.components.ConfirmDeleteDialog
import com.example.gamerent.ui.components.ErrorDialog
import com.example.gamerent.ui.components.GameDetailDialog
import com.example.gamerent.ui.screens.CatalogScreen
import com.example.gamerent.ui.screens.HomeScreen
import com.example.gamerent.ui.screens.MapScreen
import com.example.gamerent.ui.screens.ProfileScreen
import com.example.gamerent.viewmodel.GameViewModel

private val FondoColor = Color(0xFF101522)
private val TarjetaColor = Color(0xFF1C2638)
private val MoradoColor = Color(0xFF8B72FF)
private val TextoSecundarioColor = Color(0xFFADB8CC)

@Composable
fun PantallaGameRent(viewModel: GameViewModel) {
    val busqueda by viewModel.busqueda.collectAsState()
    val consola by viewModel.consola.collectAsState()
    val genero by viewModel.genero.collectAsState()
    val juegosFiltrados by viewModel.juegosFiltrados.collectAsState()
    val todosLosJuegos by viewModel.todosLosJuegos.collectAsState()
    val mostrarFormulario by viewModel.mostrarFormulario.collectAsState()
    val juegoEditar by viewModel.juegoEditar.collectAsState()
    val juegoEliminar by viewModel.juegoEliminar.collectAsState()
    val juegoDetalle by viewModel.juegoDetalle.collectAsState()
    val mensajeError by viewModel.mensajeError.collectAsState()

    var seccionActual by remember { mutableStateOf("Inicio") }

    Scaffold(
        containerColor = FondoColor,
        bottomBar = {
            NavigationBar(containerColor = TarjetaColor) {
                listOf(
                    "Inicio" to Icons.Default.Home,
                    "Catálogo" to Icons.Default.SportsEsports,
                    "Agregar" to Icons.Default.Add,
                    "Mapa" to Icons.Default.LocationOn,
                    "Perfil" to Icons.Default.Person
                ).forEach { (titulo, icono) ->
                    NavigationBarItem(
                        selected = seccionActual == titulo,
                        onClick = {
                            if (titulo == "Agregar") {
                                viewModel.abrirFormularioNuevo()
                            } else {
                                seccionActual = titulo
                            }
                        },
                        icon = { Icon(icono, contentDescription = titulo) },
                        label = { Text(titulo) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MoradoColor,
                            selectedTextColor = MoradoColor,
                            unselectedIconColor = TextoSecundarioColor,
                            unselectedTextColor = TextoSecundarioColor,
                            indicatorColor = FondoColor
                        )
                    )
                }
            }
        }
    ) { padding ->
        when (seccionActual) {
            "Inicio" -> HomeScreen(
                viewModel = viewModel,
                busqueda = busqueda,
                consola = consola,
                genero = genero,
                juegosFiltrados = juegosFiltrados,
                onAgregarClick = { viewModel.abrirFormularioNuevo() },
                paddingValues = padding
            )
            "Catálogo" -> CatalogScreen(
                viewModel = viewModel,
                busqueda = busqueda,
                consola = consola,
                genero = genero,
                juegosFiltrados = juegosFiltrados,
                paddingValues = padding
            )
            "Mapa" -> MapScreen(paddingValues = padding)
            "Perfil" -> ProfileScreen(
                totalJuegos = todosLosJuegos.size,
                juegosDisponibles = todosLosJuegos.count { it.disponible },
                paddingValues = padding
            )
        }
    }

    // Diálogo para Agregar / Editar Videojuego
    if (mostrarFormulario) {
        AddEditGameDialog(
            juegoEditar = juegoEditar,
            onDismiss = { viewModel.cerrarFormulario() },
            onSave = { nombre, cons, gen, disp, uri, type, precio ->
                viewModel.guardarVideojuego(
                    nombre = nombre,
                    consola = cons,
                    genero = gen,
                    disponible = disp,
                    mediaUri = uri,
                    mediaType = type,
                    precioAlquiler = precio
                )
            },
            onError = { err -> viewModel.mostrarError(err) }
        )
    }

    // Diálogo de Confirmación de Eliminación
    juegoEliminar?.let { juego ->
        ConfirmDeleteDialog(
            videojuego = juego,
            onConfirm = { viewModel.confirmarEliminacion() },
            onDismiss = { viewModel.cancelarEliminacion() }
        )
    }

    // Diálogo de Detalle del Videojuego
    juegoDetalle?.let { juego ->
        GameDetailDialog(
            videojuego = juego,
            onDismiss = { viewModel.cerrarDetalle() }
        )
    }

    // Diálogo de Mensajes de Error
    mensajeError?.let { msg ->
        ErrorDialog(
            mensaje = msg,
            onDismiss = { viewModel.limpiarError() }
        )
    }
}
