package com.example.gamerent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalConfiguration
import com.example.gamerent.R

private val FondoColor = Color(0xFF101522)
private val TarjetaColor = Color(0xFF1C2638)
private val MoradoColor = Color(0xFF8B72FF)
private val TextoColor = Color(0xFFF5F6FA)
private val TextoSecundarioColor = Color(0xFFADB8CC)

@Composable
fun ProfileScreen(
    totalJuegos: Int,
    juegosDisponibles: Int,
    paddingValues: PaddingValues,
    onLanguageSelected: (String) -> Unit
) {
    val currentLanguage = LocalConfiguration.current.locales[0].language
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoColor)
            .padding(paddingValues)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar e Información del Usuario
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(MoradoColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = stringResource(R.string.profile_icon_accessibility),
                tint = MoradoColor,
                modifier = Modifier.size(54.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.profile_title),
                color = TextoColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.profile_subtitle),
                color = TextoSecundarioColor,
                fontSize = 14.sp
            )
        }

        HorizontalDivider(color = TarjetaColor)

        // Tarjetas Estadísticas de Alquiler
        Text(
            text = stringResource(R.string.system_stats),
            color = TextoColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatItem(
                titulo = stringResource(R.string.registered),
                valor = "$totalJuegos",
                icono = Icons.Default.SportsEsports,
                modifier = Modifier.weight(1f)
            )
            StatItem(
                titulo = stringResource(R.string.available_count),
                valor = "$juegosDisponibles",
                icono = Icons.Default.CheckCircle,
                modifier = Modifier.weight(1f)
            )
        }

        StatItem(
            titulo = stringResource(R.string.local_database),
            valor = stringResource(R.string.room_database),
            icono = Icons.Default.Storage,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.language_label),
                color = TextoColor,
                fontWeight = FontWeight.SemiBold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = currentLanguage == "es",
                    onClick = { onLanguageSelected("es") },
                    label = { Text(stringResource(R.string.language_spanish)) }
                )
                FilterChip(
                    selected = currentLanguage == "en",
                    onClick = { onLanguageSelected("en") },
                    label = { Text(stringResource(R.string.language_english)) }
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = stringResource(R.string.profile_footer),
            color = TextoSecundarioColor,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun StatItem(
    titulo: String,
    valor: String,
    icono: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = TarjetaColor),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(MoradoColor.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = MoradoColor)
            }
            Column {
                Text(titulo, color = TextoSecundarioColor, fontSize = 12.sp)
                Text(valor, color = TextoColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
