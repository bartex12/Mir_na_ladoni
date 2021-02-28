package com.bartex.statesmvvm.dagger

import androidx.room.Room
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.model.repositories.states.cash.RoomStateCash
import com.bartex.statesmvvm.model.repositories.weather.cash.IRoomWeatherCash
import com.bartex.statesmvvm.model.repositories.weather.cash.RoomWeatherCash
import com.bartex.statesmvvm.model.room.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheModule {

    @Singleton
    @Provides
    fun database(app: App): Database =
        Room.databaseBuilder(app, Database::class.java, Database.DB_NAME).build()

    @Provides
    @Singleton
    fun stateCash(db: Database): IRoomStateCash = RoomStateCash(db)

    @Provides
    @Singleton
    fun weatherCash(db: Database): IRoomWeatherCash = RoomWeatherCash(db)


}