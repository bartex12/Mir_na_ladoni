package com.bartex.statesmvvm.view.fragments.quiz.fsm.entity

sealed class Answer{
   object NotWell: Answer()
   object WellNotLast: Answer()
   object WellAndLast: Answer()
}
