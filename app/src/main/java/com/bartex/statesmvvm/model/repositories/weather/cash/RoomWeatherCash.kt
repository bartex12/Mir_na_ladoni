package com.bartex.statesmvvm.model.repositories.weather.cash

import com.bartex.statesmvvm.model.entity.weather.Main
import com.bartex.statesmvvm.model.entity.weather.Sys
import com.bartex.statesmvvm.model.entity.weather.Weather
import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital
import com.bartex.statesmvvm.model.room.Database
import com.bartex.statesmvvm.model.room.tables.RoomWeather

class RoomWeatherCash(private val db: Database )    :IRoomWeatherCash {
    companion object{const val TAG = "33333"}

    override suspend fun doWeatherCashCoroutine(weatherInCapital: WeatherInCapital) {
      val   roomWeather = RoomWeather(
            weatherInCapital.name?:"",
            weatherInCapital.sys?.country ?:"***",
            weatherInCapital.weather?.get(0)?.description ?:"",
            weatherInCapital.main?.humidity ?:0,
            weatherInCapital.main?.pressure ?:0,
            weatherInCapital.main?.temp ?:0f,
            weatherInCapital.weather?.get(0)?.icon?:""
        )
        db.weatherDao.insertCoroutine(roomWeather)
    }

    override suspend fun getWeatherFromCashCoroutine(capital: String?): WeatherInCapital? {
        var weatherInCapital:WeatherInCapital? = null
        capital?. let{cap->
            val roomWeather =   db.weatherDao.findByNameCoroutine(cap)
            roomWeather?. let{
              weatherInCapital =   WeatherInCapital(
                    listOf(Weather(it.description, it.iconCod)),
                    Main(it.temperature, it.pressure, it.humidity),
                    Sys(it.country ),
                    it.capitalName)
            }
        }
        return weatherInCapital
    }
}

