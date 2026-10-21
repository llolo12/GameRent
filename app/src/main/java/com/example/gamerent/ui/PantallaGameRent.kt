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
import androidx.compose.ui.res.stringResource
import com.example.gamerent.R
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
fun PantallaGameRent(
    viewModel: GameViewModel,
    onLanguageSelected: (String) -> Unit
) {
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

    var seccionActual by remember { mutableStateOf("home") }

    Scaffold(
        containerColor = FondoColor,
        bottomBar = {
            NavigationBar(containerColor = TarjetaColor) {
                listOf(
                    Triple("home", R.string.nav_home, Icons.Default.Home),
                    Triple("catalog", R.string.nav_catalog, Icons.Default.SportsEsports),
                    Triple("add", R.string.nav_add, Icons.Default.Add),
                    Triple("map", R.string.nav_map, Icons.Default.LocationOn),
                    Triple("profile", R.string.nav_profile, Icons.Default.Person)
                ).forEach { (section, labelRes, icon) ->
                    val label = stringResource(labelRes)
                    NavigationBarItem(
                        selected = seccionActual == section,
                        onClick = {
                            if (section == "add") {
                                viewModel.abrirFormularioNuevo()
                            } else {
                                seccionActual = section
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
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
            "home" -> HomeScreen(
                viewModel = viewModel,
                busqueda = busqueda,
                consola = consola,
                genero = genero,
                juegosFiltrados = juegosFiltrados,
                onAgregarClick = { viewModel.abrirFormularioNuevo() },
                paddingValues = padding
            )
            "catalog" -> CatalogScreen(
                viewModel = viewModel,
                busqueda = busqueda,
                consola = consola,
                genero = genero,
                juegosFiltrados = juegosFiltrados,
                paddingValues = padding
            )
            "map" -> MapScreen(paddingValues = padding)
            "profile" -> ProfileScreen(
                totalJuegos = todosLosJuegos.size,
                juegosDisponibles = todosLosJuegos.count { it.disponible },
                paddingValues = padding,
                onLanguageSelected = onLanguageSelected
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
