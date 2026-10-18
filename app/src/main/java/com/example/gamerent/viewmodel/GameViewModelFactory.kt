package com.example.gamerent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gamerent.data.repository.VideojuegoRepository

class GameViewModelFactory(private val repository: VideojuegoRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel Class Desconocida")
    }
}
