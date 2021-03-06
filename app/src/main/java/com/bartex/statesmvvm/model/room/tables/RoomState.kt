package com.bartex.statesmvvm.model.room.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bartex.statesmvvm.common.MapOfCapital
import com.bartex.statesmvvm.common.MapOfState

/*будем использовать отдельный класс RoomState для работы с базой, чтобы не
 вносить изменений в существующие сущности во избежание создания зависимости логики от Room
 RoomState будет представлять таблицу State*/
@Entity
class RoomState(
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