package com.bartex.statesmvvm.view.fragments.help

import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.repositories.help.IHelpRepo
import javax.inject.Inject

class HelpViewModel:ViewModel() {

    @Inject
    lateinit var helpRepo: IHelpRepo

    fun getHelpText():String?{
      return  helpRepo.getHelpText()
    }
}