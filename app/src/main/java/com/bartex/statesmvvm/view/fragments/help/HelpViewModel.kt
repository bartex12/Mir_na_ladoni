package com.bartex.statesmvvm.view.fragments.help

import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.repositories.help.IHelpRepo

class HelpViewModel(private val helpRepo: IHelpRepo):ViewModel() {

    fun getHelpText(): String {
      return  helpRepo.getHelpText()
    }
}