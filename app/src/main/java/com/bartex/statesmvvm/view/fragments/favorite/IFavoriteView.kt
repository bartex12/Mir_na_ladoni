package com.bartex.statesmvvm.view.fragments.favorite

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface IFavoriteView: MvpView {
   fun init()
    fun  updateList()
}