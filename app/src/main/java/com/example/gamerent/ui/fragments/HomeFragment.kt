package com.example.gamerent.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.gamerent.R

class HomeFragment : GameListFragment(R.layout.fragment_home) {
    override val countStringRes: Int = R.string.results_count
    override val emptyStringRes: Int = R.string.empty_games

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button_add_game).setOnClickListener {
            gameViewModel.abrirFormularioNuevo()
        }
    }
}
