package com.bartex.statesmvvm.view.fragments.weather

import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital

sealed class WeatherViewStateSealed {
    data class Success(val weather: WeatherInCapital):WeatherViewStateSealed()
    data class Error(val error: Throwable):WeatherViewStateSealed()
    data class Loading(val progress: Int?):WeatherViewStateSealed()
}