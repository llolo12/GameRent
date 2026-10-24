package com.example.gamerent.ui.fragments

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamerent.MainActivity
import com.example.gamerent.R
import com.example.gamerent.ui.adapters.VideoGameAdapter
import com.example.gamerent.ui.filters.GameFiltersController
import com.example.gamerent.viewmodel.GameViewModel
import kotlinx.coroutines.launch

abstract class GameListFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {
    protected val gameViewModel: GameViewModel by lazy {
        val host = requireActivity() as MainActivity
        ViewModelProvider(host, host.gameViewModelFactory)[GameViewModel::class.java]
    }

    protected abstract val countStringRes: Int
    protected abstract val emptyStringRes: Int

    override fun onViewCreated(view: View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_games)
        val emptyMessage = view.findViewById<TextView>(R.id.text_empty_games)
        val resultCount = view.findViewById<TextView>(R.id.text_result_count)
        val adapter = VideoGameAdapter(
            onGameClick = gameViewModel::verDetalle,
            onEditClick = gameViewModel::abrirFormularioEdicion,
            onDeleteClick = gameViewModel::solicitarEliminacion
        )

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        GameFiltersController.bind(view, gameViewModel, viewLifecycleOwner)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                gameViewModel.juegosFiltrados.collect { games ->
                    adapter.submitList(games)
                    resultCount.text = getString(countStringRes, games.size)
                    emptyMessage.text = getString(emptyStringRes)
                    emptyMessage.visibility = if (games.isEmpty()) View.VISIBLE else View.GONE
                    recycler.visibility = if (games.isEmpty()) View.GONE else View.VISIBLE
                }
            }
        }
    }
}
