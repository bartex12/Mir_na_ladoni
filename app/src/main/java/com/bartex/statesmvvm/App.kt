package com.bartex.statesmvvm

import android.app.Application
import com.bartex.statesmvvm.koin.allScreen
import com.bartex.statesmvvm.koin.application
import com.bartex.statesmvvm.model.room.Database
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    companion object{
        //lateinit var instance:App
    }

    override fun onCreate() {
        super.onCreate()

        //instance =this

      startKoin {
          androidContext(applicationContext)
          modules(listOf(application, allScreen))
      }
    }
}
//androidContext(applicationContext)