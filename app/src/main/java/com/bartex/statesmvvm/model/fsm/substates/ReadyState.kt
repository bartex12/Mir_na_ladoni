package com.bartex.statesmvvm.model.fsm.substates

import com.bartex.statesmvvm.model.fsm.Action
import com.bartex.statesmvvm.model.fsm.IFlagState
import com.bartex.statesmvvm.model.fsm.entity.DataFlags
import com.bartex.statesmvvm.model.fsm.substates.NextFlagState

class ReadyState(val data: DataFlags): IFlagState {
    override fun executeAction(action: Action): IFlagState {
        return when(action){
            is Action.OnNextFlagClicked -> NextFlagState(data)
            else -> throw IllegalStateException("Invalid action $action passed to state $this")
        }
    }
}