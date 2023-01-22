package com.bartex.statesmvvm.koin

import androidx.room.Room
import com.bartex.statesmvvm.model.api.DataSourceRetrofit
import com.bartex.statesmvvm.model.api.DataWeatherRetrofit
import com.bartex.statesmvvm.model.api.IDataSource
import com.bartex.statesmvvm.model.api.IWeatherSourse
import com.bartex.statesmvvm.model.repositories.help.HelpRepo
import com.bartex.statesmvvm.model.repositories.help.IHelpRepo
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.prefs.PreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.IStatesRepo
import com.bartex.statesmvvm.model.repositories.states.StatesRepo
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.model.repositories.states.cash.RoomStateCash
import com.bartex.statesmvvm.model.repositories.weather.IWeatherRepo
import com.bartex.statesmvvm.model.repositories.weather.WeatherRepo
import com.bartex.statesmvvm.model.repositories.weather.cash.IRoomWeatherCash
import com.bartex.statesmvvm.model.repositories.weather.cash.RoomWeatherCash
import com.bartex.statesmvvm.model.room.Database
import com.bartex.statesmvvm.model.room.tables.MigrationDb
import com.bartex.statesmvvm.model.utils.IStateUtils
import com.bartex.statesmvvm.model.utils.StateUtils
import com.bartex.statesmvvm.view.fragments.details.DetailsViewModel
import com.bartex.statesmvvm.view.fragments.favorite.FavoriteViewModel
import com.bartex.statesmvvm.view.fragments.flags.FlagViewModel
import com.bartex.statesmvvm.view.fragments.help.HelpViewModel
import com.bartex.statesmvvm.view.fragments.home.HomeViewModel
import com.bartex.statesmvvm.view.fragments.home.IMainList
import com.bartex.statesmvvm.view.fragments.home.MainListImpl
import com.bartex.statesmvvm.view.fragments.quiz.flagstatemistake.FlagsQuizModel
import com.bartex.statesmvvm.view.fragments.quiz.fsm.storage.FlagQuiz
import com.bartex.statesmvvm.view.fragments.quiz.fsm.storage.IFlagQuiz
import com.bartex.statesmvvm.view.fragments.quiz.flagstatemistake.MistakesQuizModel
import com.bartex.statesmvvm.view.fragments.quiz.setting.ISettingsProvider
import com.bartex.statesmvvm.view.fragments.quiz.setting.SettingsProvider
import com.bartex.statesmvvm.view.fragments.quiz.flagstatemistake.StatesQuizModel
import com.bartex.statesmvvm.view.fragments.scheduler.SchedulerProvider
import com.bartex.statesmvvm.view.fragments.scheduler.StatesSchedulerProvider
import com.bartex.statesmvvm.view.fragments.states.StatesViewModel
import com.bartex.statesmvvm.view.fragments.weather.WeatherViewModel
import com.bartex.statesmvvm.view.main.MainViewModel
import com.bartex.statesmvvm.view.shared.SharedViewModel
import org.koin.dsl.module

val application= module {
    single <Database> {
       Room.databaseBuilder(get(), Database::class.java, Database.DB_NAME)
            .addMigrations(MigrationDb().migration1to2)
            .build()
    }
    single<IMainList> {MainListImpl(get())}
    single<IPreferenceHelper> { PreferenceHelper(get()) }
    single<IHelpRepo> { HelpRepo(get()) }
    single<IRoomStateCash> { RoomStateCash(get()) }
    single<IDataSource> {  DataSourceRetrofit()}
    single<IStatesRepo> {  StatesRepo(get(), get()) }
    single<SchedulerProvider> {  StatesSchedulerProvider() }
    single<IWeatherSourse> {  DataWeatherRetrofit() }
    single<IRoomWeatherCash> {  RoomWeatherCash(get()) }
    single<IWeatherRepo> {  WeatherRepo(get(), get()) }
    single<IStateUtils> {  StateUtils() }
    single<IFlagQuiz> {  FlagQuiz() }
    single<ISettingsProvider> {  SettingsProvider(get()) }

}

val allScreen = module {
    factory { MainViewModel() } //почемуто должен
    factory { HomeViewModel(get()) }
    factory { HelpViewModel(get()) }
    factory { StatesViewModel(get(),get(),get()) }
    factory { FavoriteViewModel(get(),get(),get(),get()) }
    factory { DetailsViewModel(get(),get(),get()) }
    factory { FlagViewModel(get(),get(),get()) }
    factory { FlagsQuizModel(get(),get(),get(),get()) }
    factory { StatesQuizModel(get(),get(),get(),get()) }
    factory { MistakesQuizModel(get(),get()) }
    factory { WeatherViewModel(get(),get(),get()) }
    factory { SharedViewModel() }

}
