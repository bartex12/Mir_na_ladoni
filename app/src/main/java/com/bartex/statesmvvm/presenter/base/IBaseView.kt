package com.bartex.statesmvvm.presenter.base

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface IBaseView: MvpView {
    fun init()
    fun updateList()
}