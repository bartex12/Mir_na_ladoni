package com.bartex.statesmvvm.view.fragments.states

import com.bartex.statesmvvm.model.entity.state.State

sealed class StatesSealed {
    data class Success(val state:List<State>):StatesSealed()
    data class Error(val error:Throwable):StatesSealed()
    data class Loading(val progress:Int?):StatesSealed()
}