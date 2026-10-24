package com.example.gamerent.ui.filters

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gamerent.R
import com.example.gamerent.domain.model.AvailabilityFilter
import com.example.gamerent.domain.model.Consoles
import com.example.gamerent.domain.model.Genres
import com.example.gamerent.viewmodel.GameViewModel
import kotlinx.coroutines.launch

object GameFiltersController {
    fun bind(root: View, viewModel: GameViewModel, lifecycleOwner: LifecycleOwner) {
        val search = root.findViewById<EditText>(R.id.edit_game_search)
        val console = root.findViewById<Spinner>(R.id.spinner_console)
        val genre = root.findViewById<Spinner>(R.id.spinner_genre)
        val availability = root.findViewById<Spinner>(R.id.spinner_availability)
        val context = root.context

        fun setOptions(spinner: Spinner, labels: List<String>) {
            spinner.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, labels).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }

        setOptions(console, listOf(context.getString(R.string.all_consoles), "PS4", "PS5"))
        setOptions(
            genre,
            listOf(
                context.getString(R.string.all_genres),
                context.getString(R.string.genre_action),
                context.getString(R.string.genre_adventure),
                context.getString(R.string.genre_racing),
                context.getString(R.string.genre_rpg),
                context.getString(R.string.genre_sports),
                context.getString(R.string.genre_strategy)
            )
        )
        setOptions(
            availability,
            listOf(
                context.getString(R.string.availability_all),
                context.getString(R.string.availability_available),
                context.getString(R.string.availability_unavailable)
            )
        )

        search.setText(viewModel.busqueda.value)
        console.setSelection(Consoles.lista.indexOf(viewModel.consola.value).coerceAtLeast(0))
        genre.setSelection(Genres.lista.indexOf(viewModel.genero.value).coerceAtLeast(0))
        availability.setSelection(
            AvailabilityFilter.entries.indexOf(viewModel.disponibilidad.value).coerceAtLeast(0)
        )

        console.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.onConsolaChanged(Consoles.lista[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
        genre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.onGeneroChanged(Genres.lista[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
        availability.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.onDisponibilidadChanged(AvailabilityFilter.entries[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
        search.doAfterTextChanged { viewModel.onBusquedaChanged(it?.toString().orEmpty()) }
        root.findViewById<View>(R.id.button_clear_filters).setOnClickListener { viewModel.resetFiltros() }

        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.busqueda.collect { value ->
                        if (search.text.toString() != value) search.setText(value)
                    }
                }
                launch { viewModel.consola.collect { console.setSelection(Consoles.lista.indexOf(it).coerceAtLeast(0)) } }
                launch { viewModel.genero.collect { genre.setSelection(Genres.lista.indexOf(it).coerceAtLeast(0)) } }
                launch {
                    viewModel.disponibilidad.collect {
                        availability.setSelection(AvailabilityFilter.entries.indexOf(it).coerceAtLeast(0))
                    }
                }
            }
        }
    }
}
