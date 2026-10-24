package com.example.gamerent.domain.repository

import com.example.gamerent.domain.model.VideoGame
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    val todosLosVideoGames: Flow<List<VideoGame>>
    suspend fun agregarVideoGame(videojuego: VideoGame): Long
    suspend fun actualizarVideoGame(videojuego: VideoGame)
    suspend fun eliminarVideoGame(videojuego: VideoGame)
}
