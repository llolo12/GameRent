package com.example.gamerent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gamerent.MainActivity
import com.example.gamerent.ui.screens.MapScreen
import com.example.gamerent.ui.screens.ProfileScreen
import com.example.gamerent.ui.theme.GameRentTheme
import com.example.gamerent.viewmodel.GameViewModel

class ComposeScreenFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val host = requireActivity() as MainActivity
        val viewModel = ViewModelProvider(host, host.gameViewModelFactory)[GameViewModel::class.java]
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                GameRentTheme {
                    if (requireArguments().getString(ARG_SCREEN) == SCREEN_MAP) {
                        MapScreen(paddingValues = PaddingValues(0.dp))
                    } else {
                        val games by viewModel.todosLosJuegos.collectAsState()
                        ProfileScreen(
                            totalJuegos = games.size,
                            juegosDisponibles = games.count { it.disponible },
                            paddingValues = PaddingValues(0.dp),
                            onLanguageSelected = host::selectAppLanguage
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val SCREEN_MAP = "map"
        const val SCREEN_PROFILE = "profile"
        private const val ARG_SCREEN = "screen"

        fun newInstance(screen: String) = ComposeScreenFragment().apply {
            arguments = Bundle().apply { putString(ARG_SCREEN, screen) }
        }
    }
}
