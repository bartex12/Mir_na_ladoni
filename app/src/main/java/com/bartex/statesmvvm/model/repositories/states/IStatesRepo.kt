package com.bartex.statesmvvm.model.repositories.states

import com.bartex.statesmvvm.model.entity.state.State
import io.reactivex.rxjava3.core.Single

interface IStatesRepo {
    fun getStates(): Single<List<State>>
    fun searchStates(search:String): Single<List<State>>
}