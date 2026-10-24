package com.example.gamerent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.gamerent.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

private val FondoColor = Color(0xFF101522)
private val TarjetaColor = Color(0xFF1C2638)
private val MoradoColor = Color(0xFF8B72FF)
private val TextoColor = Color(0xFFF5F6FA)
private val TextoSecundarioColor = Color(0xFFADB8CC)

data class SucursalGameRent(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val horario: String,
    val telefono: String,
    val ubicacion: LatLng
)

val sucursales = listOf(
    SucursalGameRent(
        id = 1,
        nombre = "GameRent Central",
        direccion = "Av. Corrientes 1234, Centro",
        horario = "10:00 - 20:00",
        telefono = "+54 11 4567-8901",
        ubicacion = LatLng(-34.6037, -58.3816)
    ),
    SucursalGameRent(
        id = 2,
        nombre = "GameRent Norte",
        direccion = "Av. Cabildo 2450, Belgrano",
        horario = "10:00 - 21:00",
        telefono = "+54 11 4567-8902",
        ubicacion = LatLng(-34.5615, -58.4560)
    ),
    SucursalGameRent(
        id = 3,
        nombre = "GameRent Sur",
        direccion = "Av. Montes de Oca 890, Barracas",
        horario = "10:00 - 19:30",
        telefono = "+54 11 4567-8903",
        ubicacion = LatLng(-34.6300, -58.3700)
    )
)

@Composable
fun MapScreen(
    paddingValues: PaddingValues
) {
    var sucursalSeleccionada by remember { mutableStateOf(sucursales.first()) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sucursalSeleccionada.ubicacion, 12f)
    }

    LaunchedEffect(sucursalSeleccionada) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(sucursalSeleccionada.ubicacion, 13f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoColor)
            .padding(paddingValues)
    ) {
        // Encabezado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Storefront,
                    contentDescription = null,
                    tint = MoradoColor,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = stringResource(R.string.branches_title),
                    color = TextoColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = stringResource(R.string.branch_subtitle),
                color = TextoSecundarioColor,
                fontSize = 13.sp
            )
        }

        // Indicaciones para configurar la API Key de Google Maps
        Card(
            colors = CardDefaults.cardColors(containerColor = MoradoColor.copy(alpha = 0.15f)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = stringResource(R.string.maps_api_accessibility),
                    tint = MoradoColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(R.string.maps_api_notice),
                    color = TextoColor,
                    fontSize = 11.sp
                )
            }
        }

        // Contenedor del Mapa Real Interactuable
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(TarjetaColor)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    compassEnabled = true
                )
            ) {
                sucursales.forEach { sucursal ->
                    Marker(
                        state = rememberMarkerState(position = sucursal.ubicacion),
                        title = sucursal.nombre,
                        snippet = sucursal.direccion,
                        onClick = {
                            sucursalSeleccionada = sucursal
                            false
                        }
                    )
                }
            }
        }

        // Selector horizontal de Sucursales y detalles de la sucursal seleccionada
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.select_branch),
                color = TextoColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(sucursales) { sucursal ->
                    val esSeleccionada = sucursal.id == sucursalSeleccionada.id
                    FilterChip(
                        selected = esSeleccionada,
                        onClick = { sucursalSeleccionada = sucursal },
                        label = { Text(sucursal.nombre) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MoradoColor,
                            selectedLabelColor = Color.White,
                            labelColor = TextoSecundarioColor,
                            selectedLeadingIconColor = Color.White
                        )
                    )
                }
            }

            // Tarjeta de detalles de la sucursal activa
            Card(
                colors = CardDefaults.cardColors(containerColor = TarjetaColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = sucursalSeleccionada.nombre,
                        color = TextoColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "📍 ${sucursalSeleccionada.direccion}",
                        color = TextoSecundarioColor,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "🕒 ${stringResource(R.string.weekday_hours, sucursalSeleccionada.horario)}",
                        color = TextoSecundarioColor,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "📞 ${sucursalSeleccionada.telefono}",
                        color = MoradoColor,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
