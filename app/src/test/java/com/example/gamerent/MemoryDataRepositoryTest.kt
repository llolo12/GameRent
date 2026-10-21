package com.example.gamerent

import com.example.gamerent.data.memory.MemoryDataRepository
import com.example.gamerent.model.Videojuego
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class MemoryDataRepositoryTest {
    @Test
    fun createUpdateDeleteEmitsSortedSnapshots() = runBlocking {
        val repository = MemoryDataRepository()
        val game = Videojuego(nombre = "Hades", consola = "PS5", genero = "RPG", disponible = true)

        val id = repository.agregarVideojuego(game)
        assertEquals(1L, id)
        assertEquals(listOf("Hades"), repository.todosLosVideojuegos.first().map { it.nombre })

        repository.actualizarVideojuego(game.copy(id = id.toInt(), nombre = "Hades II"))
        assertEquals("Hades II", repository.todosLosVideojuegos.first().single().nombre)

        repository.eliminarVideojuego(game.copy(id = id.toInt()))
        assertEquals(emptyList<Videojuego>(), repository.todosLosVideojuegos.first())
    }

    @Test
    fun preservesExistingIdsAndOrdersNewestFirst() = runBlocking {
        val repository = MemoryDataRepository(
            listOf(
                Videojuego(id = 2, nombre = "Second", consola = "PS5", genero = "RPG", disponible = true),
                Videojuego(id = 7, nombre = "Seventh", consola = "PS4", genero = "RPG", disponible = false)
            )
        )
        assertEquals(listOf(7, 2), repository.todosLosVideojuegos.first().map { it.id })
        val newId = repository.agregarVideojuego(
            Videojuego(nombre = "Next", consola = "PS5", genero = "RPG", disponible = true)
        )
        assertEquals(8L, newId)
    }
}
