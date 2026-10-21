package com.example.gamerent

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.gamerent.data.local.GameDatabase
import com.example.gamerent.data.repository.RoomGameRepository
import com.example.gamerent.ui.PantallaGameRent
import com.example.gamerent.ui.theme.GameRentTheme
import com.example.gamerent.viewmodel.GameViewModel
import com.example.gamerent.viewmodel.GameViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels {
        val database = GameDatabase.getDatabase(applicationContext)
        val repository = RoomGameRepository(database.videojuegoDao())
        GameViewModelFactory(application, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameRentTheme {
                PantallaGameRent(
                    viewModel = viewModel,
                    onLanguageSelected = { languageTag ->
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags(languageTag)
                        )
                    }
                )
            }
        }
    }
}
