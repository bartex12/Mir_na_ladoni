package com.bartex.statesmvvm.view.shared

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val newRegion = MutableLiveData<String>()
    val toolbarTitleFromFlag = MutableLiveData<String>()
    val toolbarTitleFromState = MutableLiveData<String>()
    //первое включение?
    private var firstRun = true


    fun updateRegion(currentRegion: String) {
        newRegion.value = currentRegion
    }

    fun updateToolbarTitleFromFlag(title:String){
        toolbarTitleFromFlag.value = title
    }

    fun updateToolbarTitleFromState(title:String){
        toolbarTitleFromState.value = title
    }

    fun setFirstRun(isFirst:Boolean){
        firstRun = isFirst
    }

    fun getFirstRun():Boolean{
        return firstRun
    }
}