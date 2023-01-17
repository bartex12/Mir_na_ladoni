package com.bartex.statesmvvm.model.repositories.weather

import android.util.Log
import com.bartex.statesmvvm.model.api.ApiServiceWeather
import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital
import com.bartex.statesmvvm.model.repositories.weather.cash.IRoomWeatherCash
import com.bartex.statesmvvm.model.repositories.weather.cash.RoomWeatherCash
import com.bartex.statesmvvm.model.room.Database
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class WeatherRepo(
    val api: ApiServiceWeather,
    private val  roomWeatherCash: IRoomWeatherCash = RoomWeatherCash(db = Database.getInstance() as Database)
)
    :IWeatherRepo {

    companion object{
        const val TAG = "33333"
    }
    //в зависимости от статуса сети
    //мы или получаем данные из сети, записывая их в базу данных с помощью Room через map
    //или берём из базы, преобразуя их также через map
    override fun getWeatherInCapital(
        isNetworkAvailable:Boolean, capital: String?, keyApi:String, units:String)
            : Single<WeatherInCapital> =
        if(isNetworkAvailable){
                     //Log.d(TAG, "WeatherRepo  isOnLine  = true")
                    //получаем данные из сети в виде Single<WeatherInCapital>
                    api.loadWeatherInCapitalEng(capital, keyApi, units)
                        .flatMap { weatherInCapital ->
                            Log.d(TAG, "WeatherRepo  loadWeatherInCapitalRu name = " +
                                        "${weatherInCapital.name} temp = ${weatherInCapital.main?.temp}"
                            )
                            //реализация кэширования погоды в столице из сети в базу данных
                            roomWeatherCash.doWeatherCash(weatherInCapital)
                        }
        }else{
            //Log.d(TAG, "WeatherRepo  isNetworkAvailable  = false")
            //получение погоды в столице из кэша
            roomWeatherCash.getWeatherFromCash(capital)
        }
            .subscribeOn(Schedulers.io())

}