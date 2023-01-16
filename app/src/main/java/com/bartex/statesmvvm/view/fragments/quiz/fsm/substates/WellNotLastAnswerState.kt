package com.bartex.statesmvvm.view.fragments.quiz.fsm.substates

import com.bartex.statesmvvm.view.fragments.quiz.fsm.Action
import com.bartex.statesmvvm.view.fragments.quiz.fsm.IFlagState
import com.bartex.statesmvvm.view.fragments.quiz.fsm.entity.DataFlags

class WellNotLastAnswerState(val data: DataFlags): IFlagState {
    override fun executeAction(action: Action): IFlagState {
        return when(action){
            is Action.OnNextFlagClicked -> NextFlagState(data)
            is Action.OnNotWellClicked -> NotWellAnswerState(data)
            is Action.OnWellNotLastClicked -> WellNotLastAnswerState(data)
            is Action.OnWellAndLastClicked -> WellAndLastAnswerState(data)
            else -> throw IllegalStateException("Invalid action $action passed to state $this")
        }
    }
}