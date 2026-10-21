package com.example.gamerent.domain.repository

import com.example.gamerent.model.Videojuego
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    val todosLosVideojuegos: Flow<List<Videojuego>>
    suspend fun agregarVideojuego(videojuego: Videojuego): Long
    suspend fun actualizarVideojuego(videojuego: Videojuego)
    suspend fun eliminarVideojuego(videojuego: Videojuego)
}
