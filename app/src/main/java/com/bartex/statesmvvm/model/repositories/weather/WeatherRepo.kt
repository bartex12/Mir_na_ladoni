package com.bartex.statesmvvm.model.repositories.weather

import android.util.Log
import com.bartex.statesmvvm.model.api.IDataSourceWeather
import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital
import com.bartex.statesmvvm.model.network.INetworkStatus
import com.bartex.statesmvvm.model.repositories.weather.cash.IRoomWeatherCash
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class WeatherRepo(val api: IDataSourceWeather, private val networkStatus: INetworkStatus,
                  private val  roomWeatherCash: IRoomWeatherCash):IWeatherRepo {

    companion object{
        const val TAG = "33333"
    }
    //в зависимости от статуса сети
    //мы или получаем данные из сети, записывая их в базу данных с помощью Room через map
    //или берём из базы, преобразуя их также через map
    override fun getWeatherInCapital(capital: String?,keyApi: String?,units: String?, lang:String?)
            : Single<WeatherInCapital> =
        networkStatus.isOnlineSingle()
            .flatMap {isOnLine-> //получаем доступ к Boolean значениям
                if (isOnLine){ //если сеть есть
                    Log.d(TAG, "WeatherRepo  isOnLine  = true")
                    //получаем данные из сети в виде Single<WeatherInCapital>
                    api.loadWeatherInCapitalEng(capital, keyApi, units)
                        .flatMap {weatherInCapital->
                            Log.d(TAG, "WeatherRepo  loadWeatherInCapitalRu name = " +
                                    "${weatherInCapital.name} temp = ${weatherInCapital.main?.temp}")
                            //реализация кэширования погоды в столице из сети в базу данных
                            roomWeatherCash.doWeatherCash(weatherInCapital)
                        }
                }else{
                    Log.d(TAG, "WeatherRepo  isOnLine  = false")
                    //получение погоды в столице из кэша
                    roomWeatherCash.getWeatherFromCash(capital)
                }
            }.subscribeOn(Schedulers.io())
}