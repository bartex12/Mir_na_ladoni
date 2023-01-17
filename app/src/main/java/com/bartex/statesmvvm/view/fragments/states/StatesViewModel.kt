package com.bartex.statesmvvm.view.fragments.states

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

//var mainThreadScheduler:SchedulerProvider - сделан через интерфейс для целей тестирования
//при тестировании вместо StatesSchedulerProvider будет использован класс-заглушка ScheduleProviderStub
class StatesViewModel(
    var helper : IPreferenceHelper = PreferenceHelper(App.instance),
    private var schedulerProvider: SchedulerProvider = StatesSchedulerProvider(),
    private var statesRepo: IStatesRepo =StatesRepo(
        dataSource = DataSourceRetrofit(),
        roomCash =  RoomStateCash(db = Database.getInstance() as Database)),
    private val roomCash: IRoomStateCash = RoomStateCash(db =Database.getInstance() as Database)
): ViewModel() {

    companion object{
        const val TAG = "33333"
    }

    //список стран из сети
    private val listStatesFromNet = MutableLiveData<StatesSealed>()
    //список стран из базы
    private val listStatesFromDatabase = MutableLiveData<MutableList<State>>()
    //текущий регион
    private val newRegion = MutableLiveData<String>()


    fun getStatesSealed() : LiveData<StatesSealed>{
        loadDataSealed()
        return listStatesFromNet
    }

    fun loadDataSealed(){
        listStatesFromNet.value = StatesSealed.Loading(null)

        statesRepo.getStates()
            .observeOn(schedulerProvider.computation())
            .observeOn(schedulerProvider.ui())
            .subscribe ({states->
                states.let{
                    // если данные загружены - выставляем value в MutableLiveData
                    listStatesFromNet.value = StatesSealed.Success(state = it)
                    Log.d(TAG, "StatesViewModel  loadDataSealed states.size = ${it.size}")
                }
            }, {error ->
                //если произошла ошибка - выставляем value в MutableLiveData в ошибку
                listStatesFromNet.value = StatesSealed.Error(error = error)
                Log.d(TAG, "StatesViewModel onError ${error.message}")
            })
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

    fun getDataFromDatabase(): LiveData<MutableList<State>> {
        loadDataFromDatabase()
        return listStatesFromDatabase
    }

    private fun loadDataFromDatabase() {
        roomCash.loadAllData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listStatesFromDatabase.value = it
            },{
                Log.d(TAG, "${it.message}")
            })
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