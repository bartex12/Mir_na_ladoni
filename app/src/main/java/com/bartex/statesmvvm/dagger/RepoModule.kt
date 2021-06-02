package com.bartex.statesmvvm.dagger

import com.bartex.statesmvvm.model.api.IDataSourceState
import com.bartex.statesmvvm.model.api.IDataSourceWeather
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
    fun statesRepo(api: IDataSourceState,roomCash: IRoomStateCash):IStatesRepo =
        StatesRepo(api, roomCash)

    @Provides
    @Singleton
    fun weatherRepo(api: IDataSourceWeather, roomWeatherCash: IRoomWeatherCash): IWeatherRepo =
        WeatherRepo(api, roomWeatherCash)

}