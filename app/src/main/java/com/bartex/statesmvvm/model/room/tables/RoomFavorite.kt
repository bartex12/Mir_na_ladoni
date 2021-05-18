package com.bartex.statesmvvm.model.room.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomFavorite (

    var capital :String,
    var flag :String,
    @PrimaryKey var name :String,
    var region :String,
    var population :Int,
    var area :Float,
    var lat :Float,
    var lng :Float,
    var nameRus:String,
    var capitalRus:String,
    var regionRus:String
)