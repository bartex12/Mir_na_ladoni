package com.bartex.statesmvvm.dagger

import com.bartex.statesmvvm.model.api.IDataSource
import com.bartex.statesmvvm.model.api.ApiServiceWeather
import com.bartex.statesmvvm.model.api.IWeatherSourse
import com.bartex.statesmvvm.model.repositories.states.IStatesRepo
import com.bartex.statesmvvm.model.repositories.states.StatesRepo
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.model.repositories.weather.IWeatherRepo
import com.bartex.statesmvvm.model.repositories.weather.WeatherRepo
import com.bartex.statesmvvm.model.repositories.weather.cash.IRoomWeatherCash
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepoModule {

    @Provides
    @Singleton
    fun statesRepo(api: IDataSource, roomCash: IRoomStateCash):IStatesRepo =
        StatesRepo(api, roomCash)

    @Provides
    @Singleton
    fun weatherRepo(weatherRetrofit: IWeatherSourse, roomWeatherCash: IRoomWeatherCash): IWeatherRepo =
        WeatherRepo(weatherRetrofit, roomWeatherCash)

}