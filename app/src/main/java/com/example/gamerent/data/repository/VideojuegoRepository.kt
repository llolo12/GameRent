package com.example.gamerent.data.repository

import com.example.gamerent.data.local.VideojuegoDao
import com.example.gamerent.data.local.toDomain
import com.example.gamerent.data.local.toEntity
import com.example.gamerent.model.Videojuego
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VideojuegoRepository(private val videojuegoDao: VideojuegoDao) {

    val todosLosVideojuegos: Flow<List<Videojuego>> = videojuegoDao.getAllVideojuegos()
        .map { lista -> lista.map { it.toDomain() } }

    suspend fun agregarVideojuego(videojuego: Videojuego): Long {
        return videojuegoDao.insertVideojuego(videojuego.toEntity())
    }

    suspend fun actualizarVideojuego(videojuego: Videojuego) {
        videojuegoDao.updateVideojuego(videojuego.toEntity())
    }

    suspend fun eliminarVideojuego(videojuego: Videojuego) {
        videojuegoDao.deleteVideojuego(videojuego.toEntity())
    }
}
