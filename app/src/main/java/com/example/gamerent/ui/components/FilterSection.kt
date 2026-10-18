package com.example.gamerent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamerent.model.Consolas
import com.example.gamerent.model.FiltroDisponibilidad
import com.example.gamerent.model.Generos

private val MoradoColor = Color(0xFF8B72FF)
private val TextoColor = Color(0xFFF5F6FA)
private val TextoSecundarioColor = Color(0xFFADB8CC)

@Composable
fun FilterSection(
    busqueda: String,
    onBusquedaChange: (String) -> Unit,
    consolaSeleccionada: String,
    onConsolaChange: (String) -> Unit,
    generoSeleccionado: String,
    onGeneroChange: (String) -> Unit,
    disponibilidadSeleccionada: FiltroDisponibilidad,
    onDisponibilidadChange: (FiltroDisponibilidad) -> Unit,
    onResetFiltros: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Campo de búsqueda por nombre
        OutlinedTextField(
            value = busqueda,
            onValueChange = onBusquedaChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar videojuego por nombre...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            },
            trailingIcon = if (busqueda.isNotEmpty()) {
                {
                    IconButton(onClick = { onBusquedaChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar búsqueda")
                    }
                }
            } else null,
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextoColor,
                unfocusedTextColor = TextoColor,
                focusedBorderColor = MoradoColor,
                unfocusedBorderColor = TextoSecundarioColor,
                focusedLeadingIconColor = MoradoColor,
                unfocusedLeadingIconColor = TextoSecundarioColor,
                focusedPlaceholderColor = TextoSecundarioColor,
                unfocusedPlaceholderColor = TextoSecundarioColor,
                cursorColor = MoradoColor
            )
        )

        // Título de filtros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Default.FilterList,
                    contentDescription = null,
                    tint = MoradoColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Filtros combinados",
                    color = TextoColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            TextButton(
                onClick = onResetFiltros,
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text("Limpiar filtros", color = MoradoColor, fontSize = 12.sp)
            }
        }

        // Filtro Consolas (PS4, PS5, Todas)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Consola:", color = TextoSecundarioColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Consolas.lista) { opcion ->
                    val selected = consolaSeleccionada.equals(opcion, ignoreCase = true)
                    FilterChip(
                        selected = selected,
                        onClick = { onConsolaChange(opcion) },
                        label = { Text(opcion) },
                        leadingIcon = if (selected) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MoradoColor,
                            selectedLabelColor = Color.White,
                            labelColor = TextoSecundarioColor,
                            selectedLeadingIconColor = Color.White
                        )
                    )
                }
            }
        }

        // Filtro Géneros
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Género:", color = TextoSecundarioColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Generos.lista) { opcion ->
                    val selected = generoSeleccionado.equals(opcion, ignoreCase = true)
                    FilterChip(
                        selected = selected,
                        onClick = { onGeneroChange(opcion) },
                        label = { Text(opcion) },
                        leadingIcon = if (selected) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MoradoColor,
                            selectedLabelColor = Color.White,
                            labelColor = TextoSecundarioColor,
                            selectedLeadingIconColor = Color.White
                        )
                    )
                }
            }
        }

        // Filtro Disponibilidad
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Disponibilidad:", color = TextoSecundarioColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(FiltroDisponibilidad.entries) { opcion ->
                    val selected = disponibilidadSeleccionada == opcion
                    FilterChip(
                        selected = selected,
                        onClick = { onDisponibilidadChange(opcion) },
                        label = { Text(opcion.titulo) },
                        leadingIcon = if (selected) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MoradoColor,
                            selectedLabelColor = Color.White,
                            labelColor = TextoSecundarioColor,
                            selectedLeadingIconColor = Color.White
                        )
                    )
                }
            }
        }
    }
}
