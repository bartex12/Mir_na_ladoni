package com.bartex.statesmvvm.model.api

import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital
import io.reactivex.rxjava3.core.Single

interface IWeatherSourse {
    fun getWeatherInCapital(capital: String?, keyApi:String, units:String): Single<WeatherInCapital>
}