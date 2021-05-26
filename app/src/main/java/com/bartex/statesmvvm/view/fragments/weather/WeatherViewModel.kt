package com.bartex.statesmvvm.view.fragments.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.weather.IWeatherRepo
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class WeatherViewModel:ViewModel() {

    companion object{
        const val TAG = "33333"
    }

    @Inject
    lateinit var helper : IPreferenceHelper

    @Inject
    lateinit var mainThreadScheduler: Scheduler
    @Inject
    lateinit var weatherRepo: IWeatherRepo

    private val weatherSealed= MutableLiveData<WeatherSealed>()

    fun getWeatherSealed(state: State?, isNetworkAvailable:Boolean):LiveData<WeatherSealed>{
        loadWeatherSealed(state, isNetworkAvailable)
        return weatherSealed
    }

   private fun loadWeatherSealed(state: State?, isNetworkAvailable:Boolean) {
       //начинаем загрузку данных
        weatherSealed.value = WeatherSealed.Loading(null)
        state?. let {
            weatherRepo.getWeatherInCapital(isNetworkAvailable, it.capital,
                "80bb32e4a0db84762bb04ab2bd724646", "metric", "RU")
        }?.observeOn(mainThreadScheduler)
            ?.subscribe(
                {
                    weatherSealed.value = WeatherSealed.Success(weather = it)
                },
                {error ->
                    weatherSealed.value = WeatherSealed.Error(error = error)
                    Log.d(TAG, "WeatherViewModel onError ${error.message}")}
            )
    }

    fun getRusLang():Boolean{
        return helper.getRusLang()
    }
}