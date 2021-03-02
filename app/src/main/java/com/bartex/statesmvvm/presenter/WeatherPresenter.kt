package com.bartex.statesmvvm.presenter

import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.fragments.weather.IWeatherView
import moxy.MvpPresenter

class WeatherPresenter( val state: State?): MvpPresenter<IWeatherView>() {
//    @Inject
//    lateinit var mainThreadScheduler: Scheduler
//
//    @Inject
//    lateinit var weatherRepo: IWeatherRepo
//
//    @Inject
//    lateinit var router: Router
//
//    companion object{
//        const val TAG = "33333"
//    }
//
//    override fun onFirstViewAttach() {
//        super.onFirstViewAttach()
//        Log.d(TAG, "WeatherPresenter  capital = ${state?.capital}")
//        loadWeather()
//    }
//
//    private fun loadWeather() {
//        state?. let {
//            weatherRepo.getWeatherInCapital(it.capital,
//                "80bb32e4a0db84762bb04ab2bd724646", "metric", "RU")
//        }?.observeOn(mainThreadScheduler)
//            ?.subscribe(
//                {
//                    state.name?. let{stateName->viewState.setStateName(stateName)}
//                    it.name?. let{capital->viewState.setCapitalName(capital)}
//                    it.main?.pressure?. let{pressure-> viewState.setPressure(pressure)}
//                    it.main?.humidity?. let{humidity-> viewState.setHumidity(humidity)}
//                    it.weather?.get(0)?.description?. let{description->viewState.setDescription(description)}
//                    it.main?.temp?. let { temp -> viewState.setTemp(temp)}
//                    it.weather?.get(0)?.icon?. let{icon->viewState.setIconDrawble(icon)}
//                    Log.d(TAG, "WeatherPresenter onSuccess ${it.name} ${it.main?.temp}")
//                },
//                {error -> viewState.setErrorMessage()
//                    Log.d(TAG, "WeatherPresenter onError ${error.message}")}
//            )
//    }
//
//    fun backPressed():Boolean {
//        Log.d(TAG, "DetailsPresenter backPressed ")
//        //так как карта вызывается через интент, мы не управляем экраном карты
//        //и она может появиться при нажатии на кнопку назад
//        //нужно делать свой экран с картой и заходить через API key
//        state?. let{router.backTo(Screens.DetailsScreen(it))}
//        return true
//    }
}