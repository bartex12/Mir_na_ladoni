package com.bartex.statesmvvm.dagger

import com.bartex.statesmvvm.presenter.*
import com.bartex.statesmvvm.view.main.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        CiceroneModule::class,
        CacheModule::class,
        ApiModule::class,
        RepoModule::class,
        PrefModule::class,
        HelpModule::class,
        UtilsModule::class
    ]
)

interface AppComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(mainPresenter: MainPresenter)
    fun inject(statesPresenter: StatesPresenter)
    fun inject(detailsPresenter: DetailsPresenter)
    fun inject(weatherPresenter: WeatherPresenter)
    fun inject(searchPresenter: SearchPresenter)
    fun inject(helpPresenter: HelpPresenter)
    fun inject(favoritePresenter: FavoritePresenter)

}