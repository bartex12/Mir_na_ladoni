package com.bartex.statesmvvm.view.adapter.favorite

import com.bartex.statesmvvm.view.adapter.IItemView

interface FavoritesItemView: IItemView {

    fun setName(name: String)
    fun loadFlag(url: String)
    fun setArea(area: String)
    fun setPopulation(population: String)
}