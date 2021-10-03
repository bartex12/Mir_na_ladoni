package com.bartex.statesmvvm.view.fragments.quiz.flag

import androidx.lifecycle.ViewModelProvider
import com.bartex.statesmvvm.view.fragments.quiz.base.BaseResultDialog
import com.bartex.statesmvvm.view.fragments.quiz.base.BaseViewModel

class ResultDialogFlags: BaseResultDialog() {

    override fun getCurrentViewModel(): BaseViewModel {
      return  ViewModelProvider(requireActivity()).get(FlagsQuizModel::class.java)
    }

}