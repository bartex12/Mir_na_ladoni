package com.bartex.statesmvvm.model.room

import androidx.room.RoomDatabase
import com.bartex.statesmvvm.model.room.dao.FavoriteDao
import com.bartex.statesmvvm.model.room.dao.StateDao
import com.bartex.statesmvvm.model.room.dao.WeatherDao
import com.bartex.statesmvvm.model.room.tables.RoomFavorite
import com.bartex.statesmvvm.model.room.tables.RoomState
import com.bartex.statesmvvm.model.room.tables.RoomWeather

/*
* Перечисляем сущности в аннотации, наследуемся от RoomDatabase и перечисляем DAO в виде
*  абстрактных полей.
*/
@androidx.room.Database(entities = [RoomState::class, RoomWeather::class, RoomFavorite::class], version = 1 )
abstract class Database: RoomDatabase() {
    abstract val stateDao: StateDao
    abstract val weatherDao: WeatherDao
    abstract val favoriteDao: FavoriteDao

    companion object{
        const val DB_NAME = "database.db"
    }
}