package com.bartex.statesmvvm.view.fragments.quiz.state

import com.bartex.statesmvvm.model.repositories.states.IStatesRepo
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.view.fragments.quiz.fsm.Action
import com.bartex.statesmvvm.view.fragments.quiz.fsm.entity.Answer
import com.bartex.statesmvvm.view.fragments.quiz.fsm.entity.ButtonTag
import com.bartex.statesmvvm.view.fragments.quiz.base.BaseViewModel
import com.bartex.statesmvvm.view.fragments.quiz.fsm.storage.IFlagQuiz
import com.bartex.statesmvvm.view.fragments.quiz.setting.ISettingsProvider

class StatesQuizModel(statesRepo: IStatesRepo,
                      private val storage: IFlagQuiz,
                      settingProvider: ISettingsProvider,
                      roomCash: IRoomStateCash
): BaseViewModel(statesRepo,storage,settingProvider, roomCash)  {

    //по типу ответа при щелчке по кнопке задаём состояние
    fun answerImageButtonClick( tag: ButtonTag) {
        dataFlags = storage.getTypeAnswerWithTag(tag, dataFlags)
        when(dataFlags.typeAnswer){
            Answer.NotWell -> {
                currentQuizState.value = currentState.executeAction(Action.OnNotWellClicked(dataFlags))
            }
            Answer.WellNotLast -> {
                currentQuizState.value =  currentState.executeAction(Action.OnWellNotLastClicked(dataFlags))
            }
            Answer.WellAndLast -> {
                currentQuizState.value = currentState.executeAction(Action.OnWellAndLastClicked(dataFlags))
            }
        }
    }


}