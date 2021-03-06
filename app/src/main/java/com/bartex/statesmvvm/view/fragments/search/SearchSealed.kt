package com.bartex.statesmvvm.view.fragments.search

import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.fragments.states.StatesSealed

sealed class SearchSealed {
    data class Success(val searchStates:List<State>): SearchSealed()
    data class Error(val error:Throwable): SearchSealed()
    data class Loading(val progress:Int?): SearchSealed()
}