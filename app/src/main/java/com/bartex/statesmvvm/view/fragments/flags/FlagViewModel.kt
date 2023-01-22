package com.bartex.statesmvvm.view.fragments.flags

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import kotlinx.coroutines.launch

class FlagViewModel(
   private val helper : IPreferenceHelper,
   private val roomCash: IRoomStateCash
):ViewModel() {

    companion object{
        const val TAG = "33333"
    }

    private  var listFlags = MutableLiveData<List<State>>()

    fun getStateFlags(): LiveData<List<State>> {
        getStateFlagsFromCashCoroutine()
        return listFlags
    }

    private fun getStateFlagsFromCashCoroutine() {
        val isSorted = helper.isSorted()
        val getSortCase = helper.getSortCase()
        var filtredList:List<State> = listOf()
        viewModelScope.launch {
            try {
             val listStates =    roomCash.getFlagsFromCashCoroutine()
                if(isSorted){
                    when (getSortCase) {
                        1 -> {filtredList = listStates.filter {it.population!=null}.sortedByDescending {it.population}}
                        2 -> {filtredList = listStates.filter {it.population!=null}.sortedBy {it.population}}
                        3 -> {filtredList = listStates.filter {it.area!=null}.sortedByDescending {it.area} }
                        4 -> { filtredList = listStates.filter {it.area!=null}.sortedBy {it.area} }
                        5 -> {filtredList = listStates.filter {it.population!=null && it.population >0 && it.area!=null && it.area!! >0}
                            .sortedByDescending {it.population!!.toFloat()/it.area!!}}
                        6 -> {filtredList = listStates.filter {it.population!=null && it.population >0 && it.area!=null && it.area!! >0}
                            .sortedBy {it.population!!.toFloat()/it.area!!}}
                    }
                }else{
                    filtredList = listStates.sortedBy{it.name}
                }
                listFlags.value = filtredList
            }catch (error:Exception){
                Log.d(TAG, "FlagViewModel onError ${error.message}")
            }
        }
    }

}