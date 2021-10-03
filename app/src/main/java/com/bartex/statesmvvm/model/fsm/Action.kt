package com.bartex.statesmvvm.model.fsm

import com.bartex.statesmvvm.model.fsm.entity.DataFlags

sealed class Action{
    class OnNextFlagClicked(val data: DataFlags) : Action()
    class OnWellNotLastClicked(val data: DataFlags): Action()
    class OnWellAndLastClicked(val data: DataFlags): Action()
    class OnNotWellClicked(val data: DataFlags): Action()
    object OnResetQuiz : Action()
}
