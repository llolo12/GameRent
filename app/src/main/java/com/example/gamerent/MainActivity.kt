package com.example.gamerent

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gamerent.data.local.GameDatabase
import com.example.gamerent.data.repository.RoomGameRepository
import com.example.gamerent.ui.fragments.CatalogFragment
import com.example.gamerent.ui.fragments.ComposeScreenFragment
import com.example.gamerent.ui.fragments.HomeFragment
import com.example.gamerent.ui.legacy.GameRentDialogHost
import com.example.gamerent.ui.theme.GameRentTheme
import com.example.gamerent.viewmodel.GameViewModel
import com.example.gamerent.viewmodel.GameViewModelFactory

class MainActivity : AppCompatActivity() {
    val gameViewModelFactory: ViewModelProvider.Factory by lazy {
        val database = GameDatabase.getDatabase(applicationContext)
        GameViewModelFactory(application, RoomGameRepository(database.videojuegoDao()))
    }

    val gameViewModel: GameViewModel by viewModels { gameViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ComposeView>(R.id.legacy_dialog_host).setContent {
            GameRentTheme { GameRentDialogHost(gameViewModel) }
        }
        findViewById<View>(R.id.navigation_home).setOnClickListener {
            showDestination(HomeFragment(), DESTINATION_HOME)
        }
        findViewById<View>(R.id.navigation_catalog).setOnClickListener {
            showDestination(CatalogFragment(), DESTINATION_CATALOG)
        }
        findViewById<View>(R.id.navigation_add).setOnClickListener {
            gameViewModel.abrirFormularioNuevo()
        }
        findViewById<View>(R.id.navigation_map).setOnClickListener {
            showDestination(ComposeScreenFragment.newInstance(ComposeScreenFragment.SCREEN_MAP), DESTINATION_MAP)
        }
        findViewById<View>(R.id.navigation_profile).setOnClickListener {
            showDestination(ComposeScreenFragment.newInstance(ComposeScreenFragment.SCREEN_PROFILE), DESTINATION_PROFILE)
        }

        if (savedInstanceState == null) {
            showDestination(HomeFragment(), DESTINATION_HOME)
        } else {
            restoreNavigationSelection()
        }
    }

    private fun showDestination(fragment: Fragment, destination: String) {
        val current = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (current?.tag == destination) return

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, destination)
            .commit()

        selectNavigationItem(destination)
    }

    private fun restoreNavigationSelection() {
        val destination = supportFragmentManager.findFragmentById(R.id.fragment_container)?.tag
            ?: DESTINATION_HOME
        selectNavigationItem(destination)
    }

    private fun selectNavigationItem(destination: String) {
        val selectedId = when (destination) {
            DESTINATION_HOME -> R.id.navigation_home
            DESTINATION_CATALOG -> R.id.navigation_catalog
            DESTINATION_MAP -> R.id.navigation_map
            else -> R.id.navigation_profile
        }
        listOf(R.id.navigation_home, R.id.navigation_catalog, R.id.navigation_map, R.id.navigation_profile)
            .forEach { id -> findViewById<View>(id).isSelected = id == selectedId }
    }

    fun selectAppLanguage(languageTag: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag))
    }

    companion object {
        const val DESTINATION_HOME = "home"
        const val DESTINATION_CATALOG = "catalog"
        const val DESTINATION_MAP = "map"
        const val DESTINATION_PROFILE = "profile"
    }
}
