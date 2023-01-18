package com.bartex.statesmvvm.view.fragments.help

import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.model.repositories.help.HelpRepo
import com.bartex.statesmvvm.model.repositories.help.IHelpRepo
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.prefs.PreferenceHelper
import javax.inject.Inject

class HelpViewModel(private val helpRepo: IHelpRepo):ViewModel() {

    fun getHelpText(): String {
      return  helpRepo.getHelpText()
    }
}