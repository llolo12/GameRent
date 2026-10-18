package com.example.gamerent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamerent.data.repository.VideojuegoRepository
import com.example.gamerent.model.Consolas
import com.example.gamerent.model.FiltroDisponibilidad
import com.example.gamerent.model.Generos
import com.example.gamerent.model.Videojuego
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameViewModel(private val repository: VideojuegoRepository) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _consola = MutableStateFlow(Consolas.TODAS)
    val consola: StateFlow<String> = _consola.asStateFlow()

    private val _genero = MutableStateFlow(Generos.TODOS)
    val genero: StateFlow<String> = _genero.asStateFlow()

    private val _disponibilidad = MutableStateFlow(FiltroDisponibilidad.TODOS)
    val disponibilidad: StateFlow<FiltroDisponibilidad> = _disponibilidad.asStateFlow()

    private val _mostrarFormulario = MutableStateFlow(false)
    val mostrarFormulario: StateFlow<Boolean> = _mostrarFormulario.asStateFlow()

    private val _juegoEditar = MutableStateFlow<Videojuego?>(null)
    val juegoEditar: StateFlow<Videojuego?> = _juegoEditar.asStateFlow()

    private val _juegoEliminar = MutableStateFlow<Videojuego?>(null)
    val juegoEliminar: StateFlow<Videojuego?> = _juegoEliminar.asStateFlow()

    private val _juegoDetalle = MutableStateFlow<Videojuego?>(null)
    val juegoDetalle: StateFlow<Videojuego?> = _juegoDetalle.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    val todosLosJuegos: StateFlow<List<Videojuego>> = repository.todosLosVideojuegos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val juegosFiltrados: StateFlow<List<Videojuego>> = combine(
        repository.todosLosVideojuegos,
        _busqueda,
        _consola,
        _genero,
        _disponibilidad
    ) { juegos, query, consolaSel, generoSel, dispSel ->
        juegos.filter { juego ->
            val coincideNombre = juego.nombre.contains(query.trim(), ignoreCase = true)
            val coincideConsola = consolaSel == Consolas.TODAS || juego.consola.equals(consolaSel, ignoreCase = true)
            val coincideGenero = generoSel == Generos.TODOS || juego.genero.equals(generoSel, ignoreCase = true)
            val coincideDisp = when (dispSel) {
                FiltroDisponibilidad.TODOS -> true
                FiltroDisponibilidad.DISPONIBLES -> juego.disponible
                FiltroDisponibilidad.NO_DISPONIBLES -> !juego.disponible
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

    fun onDisponibilidadChanged(nuevaDisp: FiltroDisponibilidad) {
        _disponibilidad.value = nuevaDisp
    }

    fun resetFiltros() {
        _busqueda.value = ""
        _consola.value = Consolas.TODAS
        _genero.value = Generos.TODOS
        _disponibilidad.value = FiltroDisponibilidad.TODOS
    }

    fun abrirFormularioNuevo() {
        _juegoEditar.value = null
        _mostrarFormulario.value = true
    }

    fun abrirFormularioEdicion(videojuego: Videojuego) {
        _juegoEditar.value = videojuego
        _mostrarFormulario.value = true
    }

    fun cerrarFormulario() {
        _mostrarFormulario.value = false
        _juegoEditar.value = null
    }

    fun solicitarEliminacion(videojuego: Videojuego) {
        _juegoEliminar.value = videojuego
    }

    fun cancelarEliminacion() {
        _juegoEliminar.value = null
    }

    fun verDetalle(videojuego: Videojuego) {
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

    fun guardarVideojuego(
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
            mostrarError("El nombre del videojuego no puede estar vacío.")
            return
        }

        viewModelScope.launch {
            try {
                val actual = _juegoEditar.value
                if (actual == null) {
                    val nuevo = Videojuego(
                        nombre = nombreLimpio,
                        consola = consola,
                        genero = genero,
                        disponible = disponible,
                        mediaUri = mediaUri,
                        mediaType = mediaType,
                        precioAlquiler = precioAlquiler
                    )
                    repository.agregarVideojuego(nuevo)
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
                    repository.actualizarVideojuego(editado)
                }
                cerrarFormulario()
            } catch (e: Exception) {
                mostrarError("Error al guardar el videojuego: ${e.localizedMessage}")
            }
        }
    }

    fun confirmarEliminacion() {
        val juego = _juegoEliminar.value ?: return
        viewModelScope.launch {
            try {
                repository.eliminarVideojuego(juego)
                _juegoEliminar.value = null
            } catch (e: Exception) {
                mostrarError("Error al eliminar el videojuego: ${e.localizedMessage}")
            }
        }
    }
}
