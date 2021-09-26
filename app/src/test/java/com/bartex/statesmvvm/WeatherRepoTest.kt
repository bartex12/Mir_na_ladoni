package com.bartex.statesmvvm

import com.bartex.statesmvvm.model.api.ApiServiceWeather
import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital
import com.bartex.statesmvvm.model.entity.weather.WeatherQuery
import com.bartex.statesmvvm.model.repositories.weather.WeatherRepo
import com.bartex.statesmvvm.model.repositories.weather.cash.IRoomWeatherCash
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class WeatherRepoTest {

    private lateinit var repository: WeatherRepo
    @Mock
    private lateinit var weatherApi: ApiServiceWeather
    @Mock
    private lateinit var roomWeatherCash: IRoomWeatherCash

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = WeatherRepo(weatherApi, roomWeatherCash)
    }

    @Test
    fun statesRepo_TestNetYes() {

        val weatherInCapital = WeatherInCapital()
        val capital = "Москва"

        //При вызове Репозитория возвращаем шаблонные данные
        Mockito.`when`(weatherApi.loadWeatherInCapitalEng(
            capital, WeatherQuery.keyApi, WeatherQuery.units ))
            .thenReturn(Single.just(weatherInCapital))

        //вызываем метод repository, сеть есть
        repository.getWeatherInCapital(
            true, capital,  WeatherQuery.keyApi, WeatherQuery.units)

        Mockito.verify(weatherApi, Mockito.times(1))
            .loadWeatherInCapitalEng(capital,  WeatherQuery.keyApi, WeatherQuery.units)

    }

    @Test
    fun statesRepo_TestNetNo() {

        val weatherInCapital = WeatherInCapital()
        val capital = "Москва"

        //При вызове Репозитория возвращаем шаблонные данные
        Mockito.`when`(roomWeatherCash.getWeatherFromCash(capital))
            .thenReturn(Single.just(weatherInCapital))

        //вызываем метод repository, сети нет
        repository.getWeatherInCapital(
            false, capital,  WeatherQuery.keyApi, WeatherQuery.units)

        Mockito.verify(roomWeatherCash, Mockito.times(1))
            .getWeatherFromCash(capital)
    }

}