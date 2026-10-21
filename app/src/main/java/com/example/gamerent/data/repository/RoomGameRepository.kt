package com.example.gamerent.data.repository

import com.example.gamerent.data.local.VideojuegoDao
import com.example.gamerent.data.local.toDomain
import com.example.gamerent.data.local.toEntity
import com.example.gamerent.model.Videojuego
import com.example.gamerent.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomGameRepository(private val videojuegoDao: VideojuegoDao) : GameRepository {

    override val todosLosVideojuegos: Flow<List<Videojuego>> = videojuegoDao.getAllVideojuegos()
        .map { lista -> lista.map { it.toDomain() } }

    override suspend fun agregarVideojuego(videojuego: Videojuego): Long {
        return videojuegoDao.insertVideojuego(videojuego.toEntity())
    }

    override suspend fun actualizarVideojuego(videojuego: Videojuego) {
        videojuegoDao.updateVideojuego(videojuego.toEntity())
    }

    override suspend fun eliminarVideojuego(videojuego: Videojuego) {
        videojuegoDao.deleteVideojuego(videojuego.toEntity())
    }
}
