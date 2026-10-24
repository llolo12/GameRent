package com.example.gamerent.data.memory

import com.example.gamerent.domain.repository.GameRepository
import com.example.gamerent.domain.model.VideoGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** In-memory repository for tests and previews; production continues to use Room. */
class MemoryDataRepository(initialGames: List<VideoGame> = emptyList()) : GameRepository {
    private val lock = Any()
    private var nextId = (initialGames.maxOfOrNull { it.id } ?: 0) + 1
    private val games = MutableStateFlow(initialGames.sortedByDescending { it.id })

    override val todosLosVideoGames: Flow<List<VideoGame>> = games.asStateFlow()

    override suspend fun agregarVideoGame(videojuego: VideoGame): Long = synchronized(lock) {
        val id = if (videojuego.id > 0) videojuego.id else nextId++
        if (id >= nextId) nextId = id + 1
        games.value = (games.value.filterNot { it.id == id } + videojuego.copy(id = id))
            .sortedByDescending { it.id }
        id.toLong()
    }

    override suspend fun actualizarVideoGame(videojuego: VideoGame) = synchronized(lock) {
        if (games.value.any { it.id == videojuego.id }) {
            games.value = games.value.map { if (it.id == videojuego.id) videojuego else it }
                .sortedByDescending { it.id }
        }
    }

    override suspend fun eliminarVideoGame(videojuego: VideoGame) = synchronized(lock) {
        games.value = games.value.filterNot { it.id == videojuego.id }
    }
}
