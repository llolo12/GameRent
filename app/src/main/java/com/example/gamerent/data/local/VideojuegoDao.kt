package com.example.gamerent.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VideojuegoDao {
    @Query("SELECT * FROM videojuegos ORDER BY id DESC")
    fun getAllVideojuegos(): Flow<List<VideojuegoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideojuego(videojuego: VideojuegoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videojuegos: List<VideojuegoEntity>)

    @Update
    suspend fun updateVideojuego(videojuego: VideojuegoEntity)

    @Delete
    suspend fun deleteVideojuego(videojuego: VideojuegoEntity)

    @Query("SELECT COUNT(*) FROM videojuegos")
    suspend fun getCount(): Int
}
