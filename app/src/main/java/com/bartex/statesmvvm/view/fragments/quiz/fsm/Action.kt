package com.bartex.statesmvvm.view.fragments.quiz.fsm

import com.bartex.statesmvvm.view.fragments.quiz.fsm.entity.DataFlags

sealed class Action{
    class OnNextFlagClicked(val data: DataFlags) : Action()
    class OnWellNotLastClicked(val data: DataFlags): Action()
    class OnWellAndLastClicked(val data: DataFlags): Action()
    class OnNotWellClicked(val data: DataFlags): Action()
    object OnResetQuiz : Action()
}
