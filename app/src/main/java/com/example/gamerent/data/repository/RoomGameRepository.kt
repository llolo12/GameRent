package com.example.gamerent.data.repository

import com.example.gamerent.data.local.VideoGameDao
import com.example.gamerent.data.local.toDomain
import com.example.gamerent.data.local.toEntity
import com.example.gamerent.domain.model.VideoGame
import com.example.gamerent.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomGameRepository(private val videojuegoDao: VideoGameDao) : GameRepository {

    override val todosLosVideoGames: Flow<List<VideoGame>> = videojuegoDao.getAllVideoGames()
        .map { lista -> lista.map { it.toDomain() } }

    override suspend fun agregarVideoGame(videojuego: VideoGame): Long {
        return videojuegoDao.insertVideoGame(videojuego.toEntity())
    }

    override suspend fun actualizarVideoGame(videojuego: VideoGame) {
        videojuegoDao.updateVideoGame(videojuego.toEntity())
    }

    override suspend fun eliminarVideoGame(videojuego: VideoGame) {
        videojuegoDao.deleteVideoGame(videojuego.toEntity())
    }
}
