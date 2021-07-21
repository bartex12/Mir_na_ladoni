package com.bartex.statesmvvm.view.fragments.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.model.api.IDataSourceWeather
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.entity.weather.WeatherQuery
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.prefs.PreferenceHelper
import com.bartex.statesmvvm.model.repositories.weather.IWeatherRepo
import com.bartex.statesmvvm.model.repositories.weather.WeatherRepo
import com.bartex.statesmvvm.model.repositories.weather.cash.RoomWeatherCash
import com.bartex.statesmvvm.model.room.Database
import com.bartex.statesmvvm.view.fragments.scheduler.SchedulerProvider
import com.bartex.statesmvvm.view.fragments.scheduler.StatesSchedulerProvider
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class  WeatherViewModel(
    var helper : IPreferenceHelper = PreferenceHelper(App.instance),
    var schedulerProvider: SchedulerProvider = StatesSchedulerProvider(),
    var weatherRepo: IWeatherRepo = WeatherRepo(
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()))
            .build()
            .create(IDataSourceWeather::class.java),

        RoomWeatherCash(
            Room.databaseBuilder(App.instance, Database::class.java, Database.DB_NAME).build()
        )
    )

):ViewModel() {

    companion object{
        const val TAG = "33333"
        const val baseUrl =   "https://api.openweathermap.org/"
    }

//    @Inject
//    lateinit var helper : IPreferenceHelper
//
//    @Inject
//    lateinit var mainThreadScheduler: Scheduler
//    @Inject
//    lateinit var weatherRepo: IWeatherRepo

    private val weatherSealed= MutableLiveData<WeatherSealed>()

    fun getWeatherSealed():LiveData<WeatherSealed>{
        return weatherSealed
    }

   fun loadWeatherSealed(state: State?, isNetworkAvailable:Boolean) {
       //начинаем загрузку данных
        weatherSealed.value = WeatherSealed.Loading(null)
        state?. let {
            weatherRepo.getWeatherInCapital(
                isNetworkAvailable, it.capital, WeatherQuery.keyApi, WeatherQuery.units)
        }?.observeOn(schedulerProvider.ui())
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