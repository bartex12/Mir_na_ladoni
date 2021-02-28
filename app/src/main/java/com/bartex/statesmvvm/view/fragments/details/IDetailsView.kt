package com.bartex.statesmvvm.view.fragments.details

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface IDetailsView: MvpView {
    fun setStateName()
    fun setStateRegion()
    fun setStateFlag()
    fun setStateArea(area:String)
    fun setStatePopulation(population:String)
    fun setStateCapital(capital:String)
    fun sendGeoIntent(geoCoord:String)
    fun showAddFavoriteToast()
    fun setVisibility(isFavorite:Boolean)
    fun showRemoveFavoriteToast()
}