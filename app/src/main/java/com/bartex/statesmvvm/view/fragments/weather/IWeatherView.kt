package com.bartex.statesmvvm.view.fragments.weather

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface IWeatherView: MvpView {
    fun setStateName(state: String)
    fun setCapitalName(head:String)
    fun setTemp(temp:Float)
    fun setPressure(pressure:Int)
    fun setHumidity(humidity:Int)
    fun setDescription(description:String)
    fun setIconDrawble(icon:String)
    fun setErrorMessage()
}