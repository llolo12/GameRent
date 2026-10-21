package com.example.gamerent.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.gamerent.R
import com.example.gamerent.model.Consolas
import com.example.gamerent.model.Generos

/** Localizes display labels while leaving the stored Spanish option values unchanged. */
@Composable
fun LocalizedGameOptionLabel(value: String): String {
    val resource = when (value) {
        Consolas.TODAS -> R.string.all_consoles
        Generos.TODOS -> R.string.all_genres
        Consolas.PS4 -> R.string.console_ps4
        Consolas.PS5 -> R.string.console_ps5
        Generos.ACCION -> R.string.genre_action
        Generos.AVENTURA -> R.string.genre_adventure
        Generos.CARRERAS -> R.string.genre_racing
        Generos.RPG -> R.string.genre_rpg
        Generos.DEPORTES -> R.string.genre_sports
        Generos.ESTRATEGIA -> R.string.genre_strategy
        else -> null
    }
    return if (resource == null) value else stringResource(resource)
}
