package com.bartex.statesmvvm.view.fragments.help

import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.repositories.help.IHelpRepo
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import javax.inject.Inject

class HelpViewModel:ViewModel() {

    @Inject
    lateinit var helpRepo: IHelpRepo

    @Inject
    lateinit var helper : IPreferenceHelper

    fun getHelpText(): String {
      return  helpRepo.getHelpText()
    }
}