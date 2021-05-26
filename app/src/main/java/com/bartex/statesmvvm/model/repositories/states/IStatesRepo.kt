package com.bartex.statesmvvm.model.repositories.states

import com.bartex.statesmvvm.model.entity.state.State
import io.reactivex.rxjava3.core.Single

interface IStatesRepo {

    fun getStates(isNetworkAvailable:Boolean): Single<List<State>>

    fun searchStatesFromRoomRus(search:String): Single<List<State>>
    fun searchStatesFromRoomEng(search:String): Single<List<State>>
}