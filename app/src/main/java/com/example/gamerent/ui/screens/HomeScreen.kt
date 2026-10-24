package com.example.gamerent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SportsEsports
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
import androidx.compose.ui.res.stringResource
import com.example.gamerent.R
import com.example.gamerent.domain.model.VideoGame
import com.example.gamerent.ui.components.FilterSection
import com.example.gamerent.ui.components.GameCard
import com.example.gamerent.viewmodel.GameViewModel

private val FondoColor = Color(0xFF101522)
private val MoradoColor = Color(0xFF8B72FF)
private val TextoColor = Color(0xFFF5F6FA)
private val TextoSecundarioColor = Color(0xFFADB8CC)

@Composable
fun HomeScreen(
    viewModel: GameViewModel,
    busqueda: String,
    consola: String,
    genero: String,
    juegosFiltrados: List<VideoGame>,
    onAgregarClick: () -> Unit,
    paddingValues: PaddingValues
) {
    val disponibilidad by viewModel.disponibilidad.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoColor)
            .padding(paddingValues),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado principal
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "GameRent",
                        color = TextoColor,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.home_tagline),
                        color = TextoSecundarioColor,
                        fontSize = 14.sp
                    )
                }
                Icon(
                    Icons.Default.SportsEsports,
                    contentDescription = stringResource(R.string.games_icon_accessibility),
                    tint = MoradoColor,
                    modifier = Modifier.size(38.dp)
                )
            }
        }

        // Tarjeta Banner Destacada
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MoradoColor),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.home_banner_title),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.home_banner_body),
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "PS4  •  PS5  •  ${stringResource(R.string.local_database)}",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Sección de Búsqueda y Filtros Rápidos
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

        // Encabezado de la lista
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.games),
                    color = TextoColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.results_count, juegosFiltrados.size),
                    color = TextoSecundarioColor,
                    fontSize = 13.sp
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
                            text = stringResource(R.string.empty_games),
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

        item {
            OutlinedButton(
                onClick = onAgregarClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.add_game))
            }
        }
    }
}
