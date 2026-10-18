package com.example.gamerent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gamerent.model.Videojuego

private val MoradoColor = Color(0xFF8B72FF)
private val TextoSecundarioColor = Color(0xFFADB8CC)

@Composable
fun GameDetailDialog(
    videojuego: Videojuego,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = videojuego.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Imagen / Video amplicada
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MoradoColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!videojuego.mediaUri.isNullOrBlank()) {
                        AsyncImage(
                            model = videojuego.mediaUri,
                            contentDescription = videojuego.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (videojuego.mediaType == "VIDEO") {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.4f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.PlayCircle,
                                    contentDescription = "Video",
                                    tint = Color.White,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    } else {
                        Icon(
                            Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = MoradoColor,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                // Información detallada
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Consola", color = TextoSecundarioColor, fontSize = 12.sp)
                        Text(videojuego.consola, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Column {
                        Text("Género", color = TextoSecundarioColor, fontSize = 12.sp)
                        Text(videojuego.genero, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Column {
                        Text("Precio / día", color = TextoSecundarioColor, fontSize = 12.sp)
                        Text("$${videojuego.precioAlquiler}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }

                Divider(color = MoradoColor.copy(alpha = 0.3f))

                // Estado de disponibilidad
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Estado:", fontWeight = FontWeight.SemiBold)
                    Surface(
                        color = if (videojuego.disponible) Color(0xFF64D9A2).copy(alpha = 0.2f) else Color(0xFFFF8D8D).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (videojuego.disponible) "Disponible para Alquiler" else "No disponible actualmente",
                            color = if (videojuego.disponible) Color(0xFF64D9A2) else Color(0xFFFF8D8D),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MoradoColor)
            ) {
                Text("Cerrar")
            }
        }
    )
}
