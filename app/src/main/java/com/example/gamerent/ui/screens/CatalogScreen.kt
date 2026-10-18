package com.example.gamerent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamerent.model.Videojuego
import com.example.gamerent.ui.components.FilterSection
import com.example.gamerent.ui.components.GameCard
import com.example.gamerent.viewmodel.GameViewModel

private val FondoColor = Color(0xFF101522)
private val MoradoColor = Color(0xFF8B72FF)
private val TextoColor = Color(0xFFF5F6FA)
private val TextoSecundarioColor = Color(0xFFADB8CC)

@Composable
fun CatalogScreen(
    viewModel: GameViewModel,
    busqueda: String,
    consola: String,
    genero: String,
    juegosFiltrados: List<Videojuego>,
    paddingValues: PaddingValues
) {
    val disponibilidad by viewModel.disponibilidad.collectAsState()

    Scaffold(
        containerColor = FondoColor,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.abrirFormularioNuevo() },
                containerColor = MoradoColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar videojuego")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoColor)
                .padding(paddingValues)
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Catálogo de Juegos",
                    color = TextoColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Explorá, filtrá y administrá todos los videojuegos disponibles en GameRent.",
                    color = TextoSecundarioColor,
                    fontSize = 14.sp
                )
            }

            item {
                FilterSection(
                    busqueda = busqueda,
                    onBusquedaChange = { viewModel.onBusquedaChanged(it) },
                    consolaSeleccionada = consola,
                    onConsolaChange = { viewModel.onConsolaChanged(it) },
                    generoSeleccionado = genero,
                    onGeneroChange = { viewModel.onGeneroChanged(it) },
                    disponibilidadSeleccionada = disponibilidad,
                    onDisponibilidadChange = { viewModel.onDisponibilidadChanged(it) },
                    onResetFiltros = { viewModel.resetFiltros() }
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Resultados del catálogo",
                        color = TextoColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${juegosFiltrados.size} juegos encontrados",
                        color = TextoSecundarioColor,
                        fontSize = 12.sp
                    )
                }
            }

            if (juegosFiltrados.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C2638)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No se encontraron videojuegos que coincidan con la búsqueda.",
                                color = TextoSecundarioColor,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            items(juegosFiltrados, key = { it.id }) { juego ->
                GameCard(
                    videojuego = juego,
                    onEditClick = { viewModel.abrirFormularioEdicion(juego) },
                    onDeleteClick = { viewModel.solicitarEliminacion(juego) },
                    onCardClick = { viewModel.verDetalle(juego) }
                )
            }
        }
    }
}
