package com.bartex.statesmvvm.model.repositories.weather.cash

import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital
import io.reactivex.rxjava3.core.Single

interface IRoomWeatherCash {
    suspend fun doWeatherCashCoroutine(weatherInCapital:WeatherInCapital)
    suspend fun getWeatherFromCashCoroutine(capital: String?):WeatherInCapital?
}