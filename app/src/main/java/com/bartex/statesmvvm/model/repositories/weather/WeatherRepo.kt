package com.bartex.statesmvvm.model.repositories.weather
import com.bartex.statesmvvm.model.api.IWeatherSource
import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital
import com.bartex.statesmvvm.model.repositories.weather.cash.IRoomWeatherCash

class WeatherRepo(
    private val weatherRetrofit: IWeatherSource,
    private val  roomWeatherCash: IRoomWeatherCash
):IWeatherRepo {

    companion object{
        const val TAG = "33333"
    }
    //в зависимости от статуса сети
    //мы или получаем данные из сети, записывая их в базу данных с помощью Room
    //или берём из базы
    override suspend fun getWeatherInCapitalCoroutine(
        isNetworkAvailable: Boolean,
        capital: String?,
        keyApi: String,
        units: String
    ): WeatherInCapital? {
        return if(isNetworkAvailable){
            val weatherInCapitalFromNet =  weatherRetrofit.getWeatherInCapitalCoroutine(capital, keyApi, units)
            roomWeatherCash.doWeatherCashCoroutine(weatherInCapitalFromNet)
            weatherInCapitalFromNet
        }else {
            roomWeatherCash.getWeatherFromCashCoroutine(capital)
        }
    }

}