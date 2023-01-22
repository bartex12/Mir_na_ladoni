package com.bartex.statesmvvm.view.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bartex.statesmvvm.R

class HomeViewModel(val app: Application): AndroidViewModel(app) {

    private val _mainList =  MutableLiveData<MutableList<ItemList>>()

    fun getMainList(): LiveData<MutableList<ItemList>> {
        return _mainList
    }

    fun loadData(){
        _mainList.value =getList()
    }

     private fun getList(): MutableList<ItemList> {
        return mutableListOf(
            ItemList(app.resources.getString(R.string.statesOfWorld), R.drawable.globus),
            ItemList(app.resources.getString(R.string.imageQuiz), R.drawable.single_pazzl),
            ItemList(app.resources.getString(R.string.settings),null),
            ItemList(app.resources.getString(R.string.help), null)
        )
    }
}