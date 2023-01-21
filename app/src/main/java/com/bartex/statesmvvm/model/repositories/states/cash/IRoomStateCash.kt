package com.bartex.statesmvvm.model.repositories.states.cash

import androidx.lifecycle.LiveData
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.room.tables.RoomState
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IRoomStateCash {
    fun doStatesCash(listStates:List<State>):Single<List<State>>
    fun getStatesFromCash():Single<List<State>>//получение списка пользователей из кэша
    fun addToFavorite(state: State):Completable
    fun loadFavorite():Single<List<State>>
    fun isFavorite(state: State):Single<Boolean>
    fun removeFavorite(state: State):Completable
    fun loadAllData(): Single<MutableList<State>>
    fun getFlagsFromCash():Single<List<State>>//получение списка пользователей из кэша
    fun writeMistakeInDatabase(mistakeAnswer:String): Single<Boolean> //делаeм запись в базе данных о том, что ответ неверный
    fun getMistakesFromDatabase(): Single<List<State>>
    fun  getAllMistakesLive(): LiveData<List<RoomState>> //получение списка ошибок в виде LiveData
    fun removeMistakeFromDatabase(nameRus: String): Single<Boolean> //удаляем отметку об ошибке
    fun getAllDataLive(): LiveData<List<RoomState>>

    suspend fun doStatesCashCoroutine(listStates: List<State>)
}