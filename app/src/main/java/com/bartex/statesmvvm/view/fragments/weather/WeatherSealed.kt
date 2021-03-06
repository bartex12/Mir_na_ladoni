package com.bartex.statesmvvm.view.fragments.weather

import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital

sealed class WeatherSealed {
    data class Success(val weather: WeatherInCapital):WeatherSealed()
    data class Error(val error: Throwable):WeatherSealed()
    data class Loading(val progress: Int?):WeatherSealed()
}