package com.bartex.statesmvvm.view.fragments.quiz.fsm.storage

import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.fragments.quiz.fsm.entity.ButtonTag
import com.bartex.statesmvvm.view.fragments.quiz.fsm.entity.DataFlags

interface IFlagQuiz {
    fun resetQuiz(listStates: MutableList<State>, dataFlags:DataFlags, region:String):DataFlags
    fun loadNextFlag(dataFlags:DataFlags):DataFlags
    fun getTypeAnswer(guess:String, dataFlags:DataFlags):DataFlags
    fun getTypeAnswerWithTag(tag: ButtonTag, dataFlags: DataFlags):DataFlags
}