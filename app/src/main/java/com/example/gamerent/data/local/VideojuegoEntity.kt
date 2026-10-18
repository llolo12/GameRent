package com.example.gamerent.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gamerent.model.Videojuego

@Entity(tableName = "videojuegos")
data class VideojuegoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val consola: String,
    val genero: String,
    val disponible: Boolean,
    val mediaUri: String? = null,
    val mediaType: String? = null,
    val precioAlquiler: Double = 5.0
)

fun VideojuegoEntity.toDomain(): Videojuego = Videojuego(
    id = id,
    nombre = nombre,
    consola = consola,
    genero = genero,
    disponible = disponible,
    mediaUri = mediaUri,
    mediaType = mediaType,
    precioAlquiler = precioAlquiler
)

fun Videojuego.toEntity(): VideojuegoEntity = VideojuegoEntity(
    id = id,
    nombre = nombre,
    consola = consola,
    genero = genero,
    disponible = disponible,
    mediaUri = mediaUri,
    mediaType = mediaType,
    precioAlquiler = precioAlquiler
)
