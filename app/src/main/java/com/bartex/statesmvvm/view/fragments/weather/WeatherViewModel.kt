   package com.bartex.statesmvvm.view.fragments.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.entity.weather.WeatherQuery
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.weather.IWeatherRepo
import kotlinx.coroutines.launch

   class  WeatherViewModel(
    var helper : IPreferenceHelper,
    private var weatherRepo: IWeatherRepo
)
    :ViewModel() {

    companion object{const val TAG = "33333"}
    private val weatherSealed= MutableLiveData<WeatherSealed>()

    fun getWeatherSealed():LiveData<WeatherSealed>{
        return weatherSealed
    }

   fun loadWeatherSealed(state: State, isNetworkAvailable:Boolean) {
       //начинаем загрузку данных
       weatherSealed.value = WeatherSealed.Loading(null)
       viewModelScope.launch {
           try {
            val weatherInCapital =   weatherRepo.getWeatherInCapitalCoroutine(
                isNetworkAvailable, state.capital, WeatherQuery.keyApi, WeatherQuery.units )
               weatherInCapital?. let{
                   weatherSealed.value = WeatherSealed.Success(weather = weatherInCapital)
               }
           }catch (error:Exception){
               weatherSealed.value = WeatherSealed.Error(error = error)
               Log.d(TAG, "WeatherViewModel onError ${error.message}")
           }
       }
    }

    fun getRusLang():Boolean{
        return helper.getRusLang()
    }
}