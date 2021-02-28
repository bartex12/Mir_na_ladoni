package com.bartex.statesmvvm.model.room.dao

import androidx.room.*
import com.bartex.statesmvvm.model.room.tables.RoomState

/*
*  стандартные CRUD разных вариаций для создания, чтения, обновления и удаления данных, а также
* возможность поискать пользователя по логину. С помощью встроенного шаблонизатора содержимое
*  аргумента login функции findByLogin подставится вместо :login в запрос. В функциях insert
* с помощью аргумента аннотации onConflict мы указываем, что при возникновении конфликта
* по первичному ключу необходимо заменить старое значение новым*/
@Dao
interface StateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(roomState: RoomState)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg roomState: RoomState)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(roomStates:List<RoomState>)

    @Delete
    fun delete(roomState: RoomState)

    @Delete
    fun delete(vararg roomState: RoomState)

    @Delete
    fun delete(roomStates:List<RoomState>)

    @Update
    fun update(roomState: RoomState)
    @Update
    fun update(vararg roomState: RoomState)
    @Update
    fun update(roomStates:List<RoomState>)

    @Query("SELECT * FROM RoomState")
    fun getAll():List<RoomState>

    @Query("SELECT * FROM RoomState WHERE name LIKE '%' ||:name || '%'")
    fun findByName(name:String): List<RoomState>
}