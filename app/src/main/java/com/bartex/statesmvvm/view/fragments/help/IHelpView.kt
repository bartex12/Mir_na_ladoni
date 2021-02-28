package com.bartex.statesmvvm.view.fragments.help

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface IHelpView: MvpView {
    fun setText(text:String?)
}