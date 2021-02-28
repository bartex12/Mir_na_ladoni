package com.bartex.statesmvvm.model.room.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomWeather(
    @PrimaryKey val capitalName: String,
    val country: String,
    val description: String,
    val humidity: Int,
    val pressure: Int,
    val temperature: Float,
    val iconCod: String
)
