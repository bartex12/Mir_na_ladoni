package com.bartex.statesmvvm.dagger

import com.bartex.statesmvvm.view.fragments.details.DetailsViewModel
import com.bartex.statesmvvm.view.fragments.favorite.FavoriteViewModel
import com.bartex.statesmvvm.view.fragments.help.HelpViewModel
import com.bartex.statesmvvm.view.fragments.states.StatesViewModel
import com.bartex.statesmvvm.view.fragments.weather.WeatherViewModel
import com.bartex.statesmvvm.view.main.MainViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        CacheModule::class,
        ApiModule::class,
        RepoModule::class,
        PrefModule::class,
        HelpModule::class,
        UtilsModule::class
    ]
)

interface AppComponent {

    fun inject(favoriteViewModel: FavoriteViewModel)
    fun inject(statesViewModel: StatesViewModel)
    fun inject(detailsViewModel: DetailsViewModel)
    fun inject(weatherViewModel: WeatherViewModel)
    fun inject(helpViewModel: HelpViewModel)
    fun inject(mainViewModel: MainViewModel)
}