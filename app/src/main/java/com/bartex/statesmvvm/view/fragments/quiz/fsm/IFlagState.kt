package com.bartex.statesmvvm.view.fragments.quiz.fsm

import java.io.Serializable

interface IFlagState:Serializable {
    fun executeAction(action: Action): IFlagState
}