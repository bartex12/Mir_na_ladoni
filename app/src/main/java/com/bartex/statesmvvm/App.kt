package com.bartex.statesmvvm

import android.app.Application
import com.bartex.statesmvvm.dagger.AppComponent
import com.bartex.statesmvvm.dagger.AppModule
import com.bartex.statesmvvm.dagger.DaggerAppComponent

class App : Application() {
    companion object {
        lateinit var instance: App
    }

    lateinit var appComponent: AppComponent


    override fun onCreate() {
        super.onCreate()
        instance = this //здесь определяем свойство instance - контекст приложения

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}