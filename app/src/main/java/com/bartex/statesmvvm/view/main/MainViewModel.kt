package com.bartex.statesmvvm.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import javax.inject.Inject

class MainViewModel: ViewModel() {

    companion object{
        private const val TAG = "33333"
    }

    @Inject
    lateinit var helper : IPreferenceHelper

    private val colorTheme = MutableLiveData<Int>()

    fun getColorTheme():LiveData<Int> = colorTheme

    //вынесем полученную LiveData в поле helperTheme
    //чтобы отписаться от этой LiveData при ее уничтожении
    private var helperTheme: LiveData<Int>? = null

    fun loadTheme() {
        Log.d(TAG, "***0*** MainViewModel loadTheme")
        helperTheme = helper.getTheme()
        helperTheme?.observeForever (themeObserver)
    }

    private val themeObserver = object : Observer<Int>{
        override fun onChanged(theme: Int?) {
            colorTheme.value = theme
        }
    }


    override fun onCleared() {
        //отписка от наблюдателя чтобы избежать утечки памяти
        helperTheme?.removeObserver(themeObserver)
        super.onCleared()
    }
}