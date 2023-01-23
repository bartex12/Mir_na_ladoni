package com.bartex.statesmvvm.model.api

import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital

interface IWeatherSource {
    suspend fun getWeatherInCapitalCoroutine(capital: String?, keyApi:String, units:String): WeatherInCapital
}