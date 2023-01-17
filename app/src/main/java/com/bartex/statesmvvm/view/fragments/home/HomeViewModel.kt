package com.bartex.statesmvvm.view.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.App

class HomeViewModel(
    private val mainRepo:IMainList =MainListImpl(App.instance))
    : ViewModel() {

    private val _mainList =  MutableLiveData<MutableList<ItemList>>()

    fun getMainList(): LiveData<MutableList<ItemList>> {
        return _mainList
    }

    fun loadData(){
        _mainList.value = mainRepo.getList()
    }
}