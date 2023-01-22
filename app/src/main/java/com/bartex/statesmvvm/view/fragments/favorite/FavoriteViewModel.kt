package com.bartex.statesmvvm.view.fragments.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.model.utils.IStateUtils
import com.bartex.statesmvvm.model.utils.StateUtils
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val helper : IPreferenceHelper,
    private val roomCash: IRoomStateCash,
    private val stateUtils: IStateUtils = StateUtils()
) : ViewModel() {

    companion object{
        const val TAG = "33333"
    }

    private  var listFavoriteStates = MutableLiveData<List<State>>()

    fun getFavorite():LiveData<List<State>>{
        loadFavoriteCoroutine()
        return listFavoriteStates
    }

    private fun loadFavoriteCoroutine() {
        val isSorted = helper.isSorted()
        val getSortCase = helper.getSortCase()
        var filterState:List<State> = listOf()
        viewModelScope.launch {
            try {
              val listStates =   roomCash. loadFavoriteCoroutine()
                if(isSorted){
                    when (getSortCase) {
                        1 -> {filterState = listStates.filter {it.population!=null}.sortedByDescending {it.population}}
                        2 -> {filterState = listStates.filter {it.population!=null}.sortedBy {it.population}}
                        3 -> {filterState = listStates.filter {it.area!=null}.sortedByDescending {it.area} }
                        4 -> { filterState = listStates.filter {it.area!=null}.sortedBy {it.area} }
                        5 -> {filterState = listStates.filter {it.population!=null && it.population >0 && it.area!=null && it.area!! >0}
                            .sortedByDescending {it.population!!.toFloat()/it.area!!}}
                        6 -> {filterState = listStates.filter {it.population!=null && it.population >0 && it.area!=null && it.area!! >0}
                            .sortedBy {it.population!!.toFloat()/it.area!!}}
                    }
                }else{
                    filterState = listStates.sortedBy{it.name}
                }
                Log.d(TAG, "FavoriteViewModel loadFavorite: states.size =  ${filterState.size}")
            listFavoriteStates.value = filterState
            }catch (error:Exception){
                Log.d(TAG, "FavoriteViewModel onError ${error.message}")
            }
        }
    }

    fun savePositionFavorite(position: Int){
        helper.savePositionFavorite(position)
    }

    fun getPositionFavorite(): Int{
        return helper.getPositionFavorite()
    }

    fun getArea(area:Float?) = stateUtils.getStateArea(area)
    fun  getPopulation(population: Int?) = stateUtils.getStatePopulation(population)
    fun getDensity(area: Float?, population: Int?): String = stateUtils.getStateDensity(area, population)

    fun getRusLang():Boolean{
        return helper.getRusLang()
    }
}