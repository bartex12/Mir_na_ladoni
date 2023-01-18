package com.bartex.statesmvvm.view.fragments.quiz.flag

import com.bartex.statesmvvm.view.fragments.quiz.base.BaseResultDialog
import com.bartex.statesmvvm.view.fragments.quiz.base.BaseViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ResultDialogFlags: BaseResultDialog() {

    override fun getCurrentViewModel(): BaseViewModel {
        val flagsViewModel : FlagsQuizModel by viewModel()
      return flagsViewModel
    }

}