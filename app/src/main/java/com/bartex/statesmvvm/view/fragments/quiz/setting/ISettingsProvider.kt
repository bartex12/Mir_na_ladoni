package com.bartex.statesmvvm.view.fragments.quiz.setting

import com.bartex.statesmvvm.view.fragments.quiz.fsm.entity.DataFlags

interface ISettingsProvider {
    fun updateSoundOnOff()
    fun updateNumberFlagsInQuiz(dataFlags: DataFlags): DataFlags
    fun getGuessRows(dataFlags:DataFlags):DataFlags
    fun updateImageStub(dataFlags:DataFlags):DataFlags
}