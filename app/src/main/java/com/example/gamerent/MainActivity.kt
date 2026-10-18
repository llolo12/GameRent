package com.example.gamerent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.gamerent.data.local.GameDatabase
import com.example.gamerent.data.repository.VideojuegoRepository
import com.example.gamerent.ui.PantallaGameRent
import com.example.gamerent.ui.theme.GameRentTheme
import com.example.gamerent.viewmodel.GameViewModel
import com.example.gamerent.viewmodel.GameViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels {
        val database = GameDatabase.getDatabase(applicationContext)
        val repository = VideojuegoRepository(database.videojuegoDao())
        GameViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameRentTheme {
                PantallaGameRent(viewModel = viewModel)
            }
        }
    }
}
