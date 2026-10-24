package com.example.gamerent.domain.model

/**
 * Modelo de dominio que representa un videojuego en el catálogo de GameRent.
 */
data class VideoGame(
    val id: Int = 0,
    val nombre: String,
    val consola: String,
    val genero: String,
    val disponible: Boolean,
    val mediaUri: String? = null,
    val mediaType: String? = null, // "IMAGE", "VIDEO" o null
    val precioAlquiler: Double = 5.0
)

object Consoles {
    const val TODAS = "Todas"
    const val PS4 = "PS4"
    const val PS5 = "PS5"
    val lista = listOf(TODAS, PS4, PS5)
    val opcionesFormulario = listOf(PS4, PS5)
}

object Genres {
    const val TODOS = "Todos"
    const val ACCION = "Acción"
    const val AVENTURA = "Aventura"
    const val CARRERAS = "Carreras"
    const val RPG = "RPG"
    const val DEPORTES = "Deportes"
    const val ESTRATEGIA = "Estrategia"
    val lista = listOf(TODOS, ACCION, AVENTURA, CARRERAS, RPG, DEPORTES, ESTRATEGIA)
    val opcionesFormulario = listOf(ACCION, AVENTURA, CARRERAS, RPG, DEPORTES, ESTRATEGIA)
}

enum class AvailabilityFilter(val titulo: String) {
    TODOS("Todos"),
    DISPONIBLES("Disponibles"),
    NO_DISPONIBLES("No disponibles")
}
