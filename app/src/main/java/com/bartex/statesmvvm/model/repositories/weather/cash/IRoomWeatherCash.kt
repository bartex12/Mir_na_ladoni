package com.bartex.statesmvvm.model.repositories.weather.cash

import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital
import io.reactivex.rxjava3.core.Single

interface IRoomWeatherCash {
    fun doWeatherCash(weatherInCapital:WeatherInCapital): Single<WeatherInCapital>
    fun getWeatherFromCash(capital: String?):Single<WeatherInCapital>
}