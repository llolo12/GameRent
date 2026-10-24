package com.example.gamerent.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gamerent.domain.model.VideoGame

@Entity(tableName = "videojuegos")
data class VideoGameEntity(
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

fun VideoGameEntity.toDomain(): VideoGame = VideoGame(
    id = id,
    nombre = nombre,
    consola = consola,
    genero = genero,
    disponible = disponible,
    mediaUri = mediaUri,
    mediaType = mediaType,
    precioAlquiler = precioAlquiler
)

fun VideoGame.toEntity(): VideoGameEntity = VideoGameEntity(
    id = id,
    nombre = nombre,
    consola = consola,
    genero = genero,
    disponible = disponible,
    mediaUri = mediaUri,
    mediaType = mediaType,
    precioAlquiler = precioAlquiler
)
