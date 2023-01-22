package com.bartex.statesmvvm.view.fragments.states

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.model.api.DataSourceRetrofit
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.prefs.PreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.IStatesRepo
import com.bartex.statesmvvm.model.repositories.states.StatesRepo
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.model.repositories.states.cash.RoomStateCash
import com.bartex.statesmvvm.model.room.Database
import com.bartex.statesmvvm.view.fragments.scheduler.SchedulerProvider
import com.bartex.statesmvvm.view.fragments.scheduler.StatesSchedulerProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.launch


class StatesViewModel(
    var helper : IPreferenceHelper ,
    private var statesRepo: IStatesRepo,
    private val roomCash: IRoomStateCash
): ViewModel() {

    companion object{
        const val TAG = "33333"
    }

    //список стран из сети
    private val listStatesFromNet = MutableLiveData<StatesSealed>()
    //список стран из базы
    private val listStatesFromDatabase = MutableLiveData<List<State>>()
    //текущий регион
    private val newRegion = MutableLiveData<String>()


    fun getStatesSealedCoroutine() : LiveData<StatesSealed>{
        loadDataSealedCoroutine()
        return listStatesFromNet
    }


    private fun loadDataSealedCoroutine(){
        listStatesFromNet.value = StatesSealed.Loading(null)
        viewModelScope.launch {
            try{
                val listStates = statesRepo.getStatesCoroutine()
                listStatesFromNet.value = StatesSealed.Success(state = listStates)
                Log.d(TAG, "StatesViewModel  loadDataSealed listStates.size = ${listStates.size}")
            }catch (error:Exception){
                listStatesFromNet.value = StatesSealed.Error(error = error)
                Log.d(TAG, "StatesViewModel onError ${error.message}")
            }
        }
    }

    fun getPositionState(): Int{
        return helper.getPositionState()
    }

    fun savePositionState(position: Int){
        helper.savePositionState(position)
    }

    fun getRusLang():Boolean{
       return helper.getRusLang()
    }

    fun getDataFromDatabase(): LiveData<List<State>> {
        loadDataFromDatabaseCoroutine()
        return listStatesFromDatabase
    }

    private fun loadDataFromDatabaseCoroutine() {
        viewModelScope.launch {
            try{
                val list = roomCash.loadAllDataCoroutine()
                listStatesFromDatabase.value = list
            }catch (error:Exception){
                Log.d(TAG, "${error.message}")
            }
        }
    }

    fun isSorted(): Boolean{
        return helper.isSorted()
    }

    fun getSortCase():Int{
        return helper.getSortCase()
    }

    fun getNewRegion(): LiveData<String>{
        return newRegion
    }

    fun updateRegion(currentRegion: String) {
        newRegion.value = currentRegion
    }

}