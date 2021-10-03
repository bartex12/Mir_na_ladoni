package com.bartex.statesmvvm.model.fsm.substates

import com.bartex.statesmvvm.model.fsm.Action
import com.bartex.statesmvvm.model.fsm.IFlagState
import com.bartex.statesmvvm.model.fsm.entity.DataFlags

class NotWellAnswerState(val data: DataFlags): IFlagState {
    override fun executeAction(action: Action): IFlagState {
        return when(action){
            is Action.OnNotWellClicked -> NotWellAnswerState(data)
            is Action.OnWellNotLastClicked -> WellNotLastAnswerState(data)
            is Action.OnWellAndLastClicked -> WellAndLastAnswerState(data)
            else -> throw IllegalStateException("Invalid action $action passed to state $this")
        }
    }
}