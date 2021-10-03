package com.bartex.statesmvvm.view.fragments.quiz.state

import com.bartex.statesmvvm.model.fsm.Action
import com.bartex.statesmvvm.model.fsm.entity.Answer
import com.bartex.statesmvvm.model.fsm.entity.ButtonTag
import com.bartex.statesmvvm.view.fragments.quiz.base.BaseViewModel

class StatesQuizModel: BaseViewModel()  {

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