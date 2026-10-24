package com.example.gamerent.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.gamerent.R
import com.example.gamerent.domain.model.Consoles
import com.example.gamerent.domain.model.Genres

/** Localizes display labels while leaving the stored Spanish option values unchanged. */
@Composable
fun LocalizedGameOptionLabel(value: String): String {
    val resource = when (value) {
        Consoles.TODAS -> R.string.all_consoles
        Genres.TODOS -> R.string.all_genres
        Consoles.PS4 -> R.string.console_ps4
        Consoles.PS5 -> R.string.console_ps5
        Genres.ACCION -> R.string.genre_action
        Genres.AVENTURA -> R.string.genre_adventure
        Genres.CARRERAS -> R.string.genre_racing
        Genres.RPG -> R.string.genre_rpg
        Genres.DEPORTES -> R.string.genre_sports
        Genres.ESTRATEGIA -> R.string.genre_strategy
        else -> null
    }
    return if (resource == null) value else stringResource(resource)
}
