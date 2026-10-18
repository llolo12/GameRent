package com.example.gamerent.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [VideojuegoEntity::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {

    abstract fun videojuegoDao(): VideojuegoDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "gamerent_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.videojuegoDao())
                    }
                }
            }

            suspend fun populateDatabase(dao: VideojuegoDao) {
                val juegosIniciales = listOf(
                    VideojuegoEntity(nombre = "God of War Ragnarök", consola = "PS5", genero = "Aventura", disponible = true, precioAlquiler = 7.50),
                    VideojuegoEntity(nombre = "Spider-Man 2", consola = "PS5", genero = "Acción", disponible = true, precioAlquiler = 8.00),
                    VideojuegoEntity(nombre = "Gran Turismo 7", consola = "PS5", genero = "Carreras", disponible = false, precioAlquiler = 6.00),
                    VideojuegoEntity(nombre = "God of War", consola = "PS4", genero = "Aventura", disponible = true, precioAlquiler = 4.50),
                    VideojuegoEntity(nombre = "Ghost of Tsushima", consola = "PS4", genero = "Acción", disponible = true, precioAlquiler = 5.00),
                    VideojuegoEntity(nombre = "Crash Team Racing", consola = "PS4", genero = "Carreras", disponible = false, precioAlquiler = 3.50)
                )
                dao.insertAll(juegosIniciales)
            }
        }
    }
}
