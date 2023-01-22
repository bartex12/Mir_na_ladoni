package com.bartex.statesmvvm.model.api

import com.bartex.statesmvvm.model.entity.state.State
import io.reactivex.rxjava3.core.Single

interface IDataSource {
   suspend fun getStatesCoroutine(): List<State>
}