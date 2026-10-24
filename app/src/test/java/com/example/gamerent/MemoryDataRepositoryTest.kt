package com.example.gamerent

import com.example.gamerent.data.memory.MemoryDataRepository
import com.example.gamerent.domain.model.VideoGame
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class MemoryDataRepositoryTest {
    @Test
    fun createUpdateDeleteEmitsSortedSnapshots() = runBlocking {
        val repository = MemoryDataRepository()
        val game = VideoGame(nombre = "Hades", consola = "PS5", genero = "RPG", disponible = true)

        val id = repository.agregarVideoGame(game)
        assertEquals(1L, id)
        assertEquals(listOf("Hades"), repository.todosLosVideoGames.first().map { it.nombre })

        repository.actualizarVideoGame(game.copy(id = id.toInt(), nombre = "Hades II"))
        assertEquals("Hades II", repository.todosLosVideoGames.first().single().nombre)

        repository.eliminarVideoGame(game.copy(id = id.toInt()))
        assertEquals(emptyList<VideoGame>(), repository.todosLosVideoGames.first())
    }

    @Test
    fun preservesExistingIdsAndOrdersNewestFirst() = runBlocking {
        val repository = MemoryDataRepository(
            listOf(
                VideoGame(id = 2, nombre = "Second", consola = "PS5", genero = "RPG", disponible = true),
                VideoGame(id = 7, nombre = "Seventh", consola = "PS4", genero = "RPG", disponible = false)
            )
        )
        assertEquals(listOf(7, 2), repository.todosLosVideoGames.first().map { it.id })
        val newId = repository.agregarVideoGame(
            VideoGame(nombre = "Next", consola = "PS5", genero = "RPG", disponible = true)
        )
        assertEquals(8L, newId)
    }
}
