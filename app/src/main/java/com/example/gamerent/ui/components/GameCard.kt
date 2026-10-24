package com.example.gamerent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.gamerent.R
import com.example.gamerent.domain.model.VideoGame

private val TarjetaColor = Color(0xFF1C2638)
private val MoradoColor = Color(0xFF8B72FF)
private val TextoColor = Color(0xFFF5F6FA)
private val TextoSecundarioColor = Color(0xFFADB8CC)

@Composable
fun GameCard(
    videojuego: VideoGame,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = TarjetaColor),
        shape = RoundedCornerShape(18.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MoradoColor.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp)),
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
                                .background(Color.Black.copy(alpha = 0.35f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.PlayCircle,
                                contentDescription = stringResource(R.string.video_accessibility),
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                } else {
                    Icon(
                        Icons.Default.SportsEsports,
                        contentDescription = null,
                        tint = MoradoColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = videojuego.nombre,
                    color = TextoColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "${LocalizedGameOptionLabel(videojuego.consola)} • ${LocalizedGameOptionLabel(videojuego.genero)}",
                    color = TextoSecundarioColor,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(if (videojuego.disponible) R.string.available else R.string.unavailable),
                        color = if (videojuego.disponible) Color(0xFF64D9A2) else Color(0xFFFF8D8D),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = stringResource(R.string.video_price_per_day, videojuego.precioAlquiler),
                        color = TextoSecundarioColor,
                        fontSize = 12.sp
                    )
                }
            }

            Row {
                IconButton(onClick = onEditClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_accessibility),
                        tint = MoradoColor
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = stringResource(R.string.delete_accessibility),
                        tint = TextoSecundarioColor
                    )
                }
            }
        }
    }
}
