package com.bartex.statesmvvm.model.repositories.weather

import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital
import com.bartex.statesmvvm.model.entity.weather.WeatherQuery
import io.reactivex.rxjava3.core.Single

interface IWeatherRepo {
   suspend fun getWeatherInCapitalCoroutine(
        isNetworkAvailable:Boolean,  capital: String?, keyApi:String, units:String)
            : WeatherInCapital?
}