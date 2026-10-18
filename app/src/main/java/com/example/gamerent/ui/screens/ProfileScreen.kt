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

private val FondoColor = Color(0xFF101522)
private val TarjetaColor = Color(0xFF1C2638)
private val MoradoColor = Color(0xFF8B72FF)
private val TextoColor = Color(0xFFF5F6FA)
private val TextoSecundarioColor = Color(0xFFADB8CC)

@Composable
fun ProfileScreen(
    totalJuegos: Int,
    juegosDisponibles: Int,
    paddingValues: PaddingValues
) {
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
                contentDescription = "Perfil",
                tint = MoradoColor,
                modifier = Modifier.size(54.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Perfil GameRent",
                color = TextoColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Catálogo de alquiler para PS4 y PS5",
                color = TextoSecundarioColor,
                fontSize = 14.sp
            )
        }

        HorizontalDivider(color = TarjetaColor)

        // Tarjetas Estadísticas de Alquiler
        Text(
            text = "Estadísticas del sistema",
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
                titulo = "Registrados",
                valor = "$totalJuegos",
                icono = Icons.Default.SportsEsports,
                modifier = Modifier.weight(1f)
            )
            StatItem(
                titulo = "Disponibles",
                valor = "$juegosDisponibles",
                icono = Icons.Default.CheckCircle,
                modifier = Modifier.weight(1f)
            )
        }

        StatItem(
            titulo = "Base de datos local",
            valor = "Room SQLite Activa",
            icono = Icons.Default.Storage,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = "GameRent App v1.0 • Proyecto Académico",
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
