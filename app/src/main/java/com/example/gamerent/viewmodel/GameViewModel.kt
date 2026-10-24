package com.example.gamerent.viewmodel

import androidx.lifecycle.viewModelScope
import android.app.Application
import android.content.res.Configuration
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import com.example.gamerent.domain.repository.GameRepository
import com.example.gamerent.domain.model.Consoles
import com.example.gamerent.domain.model.AvailabilityFilter
import com.example.gamerent.domain.model.Genres
import com.example.gamerent.domain.model.VideoGame
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameViewModel(application: Application, private val repository: GameRepository) : AndroidViewModel(application) {

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _consola = MutableStateFlow(Consoles.TODAS)
    val consola: StateFlow<String> = _consola.asStateFlow()

    private val _genero = MutableStateFlow(Genres.TODOS)
    val genero: StateFlow<String> = _genero.asStateFlow()

    private val _disponibilidad = MutableStateFlow(AvailabilityFilter.TODOS)
    val disponibilidad: StateFlow<AvailabilityFilter> = _disponibilidad.asStateFlow()

    private val _mostrarFormulario = MutableStateFlow(false)
    val mostrarFormulario: StateFlow<Boolean> = _mostrarFormulario.asStateFlow()

    private val _juegoEditar = MutableStateFlow<VideoGame?>(null)
    val juegoEditar: StateFlow<VideoGame?> = _juegoEditar.asStateFlow()

    private val _juegoEliminar = MutableStateFlow<VideoGame?>(null)
    val juegoEliminar: StateFlow<VideoGame?> = _juegoEliminar.asStateFlow()

    private val _juegoDetalle = MutableStateFlow<VideoGame?>(null)
    val juegoDetalle: StateFlow<VideoGame?> = _juegoDetalle.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    val todosLosJuegos: StateFlow<List<VideoGame>> = repository.todosLosVideoGames
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val juegosFiltrados: StateFlow<List<VideoGame>> = combine(
        repository.todosLosVideoGames,
        _busqueda,
        _consola,
        _genero,
        _disponibilidad
    ) { juegos, query, consolaSel, generoSel, dispSel ->
        juegos.filter { juego ->
            val coincideNombre = juego.nombre.contains(query.trim(), ignoreCase = true)
            val coincideConsola = consolaSel == Consoles.TODAS || juego.consola.equals(consolaSel, ignoreCase = true)
            val coincideGenero = generoSel == Genres.TODOS || juego.genero.equals(generoSel, ignoreCase = true)
            val coincideDisp = when (dispSel) {
                AvailabilityFilter.TODOS -> true
                AvailabilityFilter.DISPONIBLES -> juego.disponible
                AvailabilityFilter.NO_DISPONIBLES -> !juego.disponible
            }
            coincideNombre && coincideConsola && coincideGenero && coincideDisp
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onBusquedaChanged(nuevaBusqueda: String) {
        _busqueda.value = nuevaBusqueda
    }

    fun onConsolaChanged(nuevaConsola: String) {
        _consola.value = nuevaConsola
    }

    fun onGeneroChanged(nuevoGenero: String) {
        _genero.value = nuevoGenero
    }

    fun onDisponibilidadChanged(nuevaDisp: AvailabilityFilter) {
        _disponibilidad.value = nuevaDisp
    }

    fun resetFiltros() {
        _busqueda.value = ""
        _consola.value = Consoles.TODAS
        _genero.value = Genres.TODOS
        _disponibilidad.value = AvailabilityFilter.TODOS
    }

    fun abrirFormularioNuevo() {
        _juegoEditar.value = null
        _mostrarFormulario.value = true
    }

    fun abrirFormularioEdicion(videojuego: VideoGame) {
        _juegoEditar.value = videojuego
        _mostrarFormulario.value = true
    }

    fun cerrarFormulario() {
        _mostrarFormulario.value = false
        _juegoEditar.value = null
    }

    fun solicitarEliminacion(videojuego: VideoGame) {
        _juegoEliminar.value = videojuego
    }

    fun cancelarEliminacion() {
        _juegoEliminar.value = null
    }

    fun verDetalle(videojuego: VideoGame) {
        _juegoDetalle.value = videojuego
    }

    fun cerrarDetalle() {
        _juegoDetalle.value = null
    }

    fun mostrarError(mensaje: String) {
        _mensajeError.value = mensaje
    }

    fun limpiarError() {
        _mensajeError.value = null
    }

    fun guardarVideoGame(
        nombre: String,
        consola: String,
        genero: String,
        disponible: Boolean,
        mediaUri: String?,
        mediaType: String?,
        precioAlquiler: Double = 5.0
    ) {
        val nombreLimpio = nombre.trim()
        if (nombreLimpio.isBlank()) {
            mostrarError(localizedString(com.example.gamerent.R.string.name_required_error))
            return
        }

        viewModelScope.launch {
            try {
                val actual = _juegoEditar.value
                if (actual == null) {
                    val nuevo = VideoGame(
                        nombre = nombreLimpio,
                        consola = consola,
                        genero = genero,
                        disponible = disponible,
                        mediaUri = mediaUri,
                        mediaType = mediaType,
                        precioAlquiler = precioAlquiler
                    )
                    repository.agregarVideoGame(nuevo)
                } else {
                    val editado = actual.copy(
                        nombre = nombreLimpio,
                        consola = consola,
                        genero = genero,
                        disponible = disponible,
                        mediaUri = mediaUri,
                        mediaType = mediaType,
                        precioAlquiler = precioAlquiler
                    )
                    repository.actualizarVideoGame(editado)
                }
                cerrarFormulario()
            } catch (e: Exception) {
                mostrarError(localizedString(com.example.gamerent.R.string.save_game_error, e.localizedMessage.orEmpty()))
            }
        }
    }

    fun confirmarEliminacion() {
        val juego = _juegoEliminar.value ?: return
        viewModelScope.launch {
            try {
                repository.eliminarVideoGame(juego)
                _juegoEliminar.value = null
            } catch (e: Exception) {
                mostrarError(localizedString(com.example.gamerent.R.string.delete_game_error, e.localizedMessage.orEmpty()))
            }
        }
    }

    private fun localizedString(resourceId: Int, vararg arguments: Any): String {
        val application = getApplication<Application>()
        val languageTags = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        val localizedContext = if (languageTags.isEmpty()) {
            application
        } else {
            val configuration = Configuration(application.resources.configuration).apply {
                setLocales(LocaleList.forLanguageTags(languageTags))
            }
            application.createConfigurationContext(configuration)
        }
        return localizedContext.getString(resourceId, *arguments)
    }
}
