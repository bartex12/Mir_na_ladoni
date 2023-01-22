package com.bartex.statesmvvm.model.repositories.states

import com.bartex.statesmvvm.model.entity.state.State
import io.reactivex.rxjava3.core.Single

interface IStatesRepo {
   suspend fun getStatesCoroutine():List<State>
}