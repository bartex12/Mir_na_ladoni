package com.bartex.statesmvvm.view.fragments.quiz.state

import androidx.lifecycle.ViewModelProvider
import com.bartex.statesmvvm.view.fragments.quiz.base.BaseResultDialog
import com.bartex.statesmvvm.view.fragments.quiz.base.BaseViewModel

class ResultDialogState: BaseResultDialog() {
    override fun getCurrentViewModel(): BaseViewModel {
      return ViewModelProvider(requireActivity()).get(StatesQuizModel::class.java)
    }
}