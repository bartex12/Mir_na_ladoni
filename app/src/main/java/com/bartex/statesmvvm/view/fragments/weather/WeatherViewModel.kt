package com.bartex.statesmvvm.view.fragments.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.weather.IWeatherRepo
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class WeatherViewModel:ViewModel() {

    companion object{
        const val TAG = "33333"
    }

    private val weatherInCapital= MutableLiveData<WeatherViewState>()

    fun getWeather():LiveData<WeatherViewState> = weatherInCapital

    @Inject
    lateinit var mainThreadScheduler: Scheduler
    @Inject
    lateinit var weatherRepo: IWeatherRepo

     fun loadWeather(state: State?) {
        state?. let {
            weatherRepo.getWeatherInCapital(it.capital,
                "80bb32e4a0db84762bb04ab2bd724646", "metric", "RU")
        }?.observeOn(mainThreadScheduler)
            ?.subscribe(
                {
                    weatherInCapital.value = WeatherViewState(weather = it)
//                    state.name?. let{stateName->viewState.setStateName(stateName)}
//                    it.name?. let{capital->viewState.setCapitalName(capital)}
//                    it.main?.pressure?. let{pressure-> viewState.setPressure(pressure)}
//                    it.main?.humidity?. let{humidity-> viewState.setHumidity(humidity)}
//                    it.weather?.get(0)?.description?. let{description->viewState.setDescription(description)}
//                    it.main?.temp?. let { temp -> viewState.setTemp(temp)}
//                    it.weather?.get(0)?.icon?. let{icon->viewState.setIconDrawble(icon)}
//                    Log.d(WeatherPresenter.TAG, "WeatherPresenter onSuccess ${it.name} ${it.main?.temp}")
                },
                {error ->
//                    viewState.setErrorMessage()
                    weatherInCapital.value = WeatherViewState(error = error)
                    Log.d(TAG, "WeatherViewModel onError ${error.message}")}
            )
    }
}