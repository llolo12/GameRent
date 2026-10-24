package com.example.gamerent.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoGameDao {
    @Query("SELECT * FROM videojuegos ORDER BY id DESC")
    fun getAllVideoGames(): Flow<List<VideoGameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoGame(videojuego: VideoGameEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videojuegos: List<VideoGameEntity>)

    @Update
    suspend fun updateVideoGame(videojuego: VideoGameEntity)

    @Delete
    suspend fun deleteVideoGame(videojuego: VideoGameEntity)

    @Query("SELECT COUNT(*) FROM videojuegos")
    suspend fun getCount(): Int
}
