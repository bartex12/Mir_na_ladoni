package com.bartex.statesmvvm.model.repositories.states.cash

import androidx.lifecycle.LiveData
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.room.tables.RoomState
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IRoomStateCash {
    fun doStatesCash(listStates:List<State>):Single<List<State>>
    fun loadAllData(): Single<MutableList<State>>
    fun  getAllMistakesLive(): LiveData<List<RoomState>> //получение списка ошибок в виде LiveData
    fun getAllDataLive(): LiveData<List<RoomState>>

    suspend fun doStatesCashCoroutine(listStates: List<State>)
    suspend fun removeMistakeFromDatabaseCoroutine(nameRus: String): Boolean //удаляем отметку об ошибке
    suspend fun writeMistakeInDatabaseCoroutine(mistakeAnswer: String): Boolean //ставим отметку об ошибке
    suspend fun getFlagsFromCashCoroutine():List<State>//получение списка пользователей из кэша
    suspend fun loadFavoriteCoroutine():List<State>
    suspend fun isFavoriteCoroutine(state: State):Boolean
    suspend fun addToFavoriteCoroutine(state: State)
    suspend fun removeFavoriteCoroutine(state: State)

}