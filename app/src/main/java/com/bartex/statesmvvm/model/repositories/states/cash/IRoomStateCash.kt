package com.bartex.statesmvvm.model.repositories.states.cash

import com.bartex.statesmvvm.model.entity.state.State
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IRoomStateCash {
    fun doStatesCash(listStates:List<State>):Single<List<State>>
    fun getStatesFromCash():Single<List<State>>//получение списка пользователей из кэша
    fun getSearchedStatesFromCash(search:String):Single<List<State>>
    fun addToFavorite(state:State):Completable
    fun loadFavorite():Single<List<State>>
    fun isFavorite(state:State):Single<Boolean>
    fun removeFavorite(state:State):Completable
}