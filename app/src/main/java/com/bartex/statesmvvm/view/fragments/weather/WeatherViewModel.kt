   package com.bartex.statesmvvm.view.fragments.weather

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.entity.weather.WeatherQuery
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.weather.IWeatherRepo
import io.reactivex.rxjava3.schedulers.Schedulers

   class  WeatherViewModel(
    var helper : IPreferenceHelper,
    private var weatherRepo: IWeatherRepo
)
    :ViewModel() {

    companion object{
        const val TAG = "33333"
        const val baseUrl =   "https://api.openweathermap.org/"
    }

    private val weatherSealed= MutableLiveData<WeatherSealed>()

    fun getWeatherSealed():LiveData<WeatherSealed>{
        return weatherSealed
    }

   fun loadWeatherSealed(state: State, isNetworkAvailable:Boolean) {
       //начинаем загрузку данных
        weatherSealed.value = WeatherSealed.Loading(null)
           weatherRepo.getWeatherInCapital(
               isNetworkAvailable,
               state.capital,
               WeatherQuery.keyApi,
               WeatherQuery.units
           ).observeOn(Schedulers.computation())
           .subscribe(
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