package com.example.gamerent.ui.fragments

import com.example.gamerent.R

class CatalogFragment : GameListFragment(R.layout.fragment_catalog) {
    override val countStringRes: Int = R.string.games_found
    override val emptyStringRes: Int = R.string.empty_search
}
