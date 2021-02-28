package com.bartex.statesmvvm.dagger

import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.model.api.IDataSourceState
import com.bartex.statesmvvm.model.api.IDataSourceWeather
import com.bartex.statesmvvm.model.network.INetworkStatus
import com.bartex.statesmvvm.model.network.NetworkStatus
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {

    @Named("baseUrlStates")
    @Provides
    fun baseUrlStates():String = "https://restcountries.eu/rest/v2/"

    @Named("baseUrlWeather")
    @Provides
    fun baseUrlWeather():String = "https://api.openweathermap.org/"

    @Provides
    @Singleton
    fun gson():Gson =
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .excludeFieldsWithoutExposeAnnotation()
            .create()

    @Provides
    @Singleton
    fun apiStates(@Named("baseUrlStates") baseUrl: String, gson: Gson): IDataSourceState =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(IDataSourceState::class.java)

    @Provides
    @Singleton
    fun apiWeather(@Named("baseUrlWeather") baseUrl: String, gson: Gson): IDataSourceWeather =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(IDataSourceWeather::class.java)

    @Singleton
    @Provides
    fun networkStatus(app: App): INetworkStatus = NetworkStatus(app)
}