package com.bartex.statesmvvm.view.fragments.quiz.state

import com.bartex.statesmvvm.view.fragments.quiz.base.BaseResultDialog
import com.bartex.statesmvvm.view.fragments.quiz.base.BaseViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ResultDialogState: BaseResultDialog() {

    override fun getCurrentViewModel(): BaseViewModel {
        val statesQuizModel : StatesQuizModel by viewModel()
        return statesQuizModel
    }
}