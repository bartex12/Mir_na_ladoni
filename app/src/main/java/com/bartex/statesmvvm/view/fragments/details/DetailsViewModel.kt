package com.bartex.statesmvvm.view.fragments.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.model.utils.IStateUtils
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val helper : IPreferenceHelper,
    private val roomCash: IRoomStateCash,
    private val stateUtils: IStateUtils
):ViewModel() {

    companion object{
        const val TAG = "33333"
    }

    private val isFavorite = MutableLiveData<DetailsSealed>()

    fun  getStateArea(area:Float?) = stateUtils.getStateArea(area)

    fun getStatePopulation(population:Int?) = stateUtils.getStatePopulation(population)

    fun getStateDensity(area: Float?, population: Int?):String =
        stateUtils.getStateDensity(area,population)

    fun getStateCapital(capital:String?) = stateUtils.getStateCapital(capital)

    fun getStateRegion(region:String?) = stateUtils.getStateRegion(region)

    fun getStateZoom(state: State) = stateUtils.getStatezoom(state)

    fun isFavoriteStateCoroutine(state: State):LiveData<DetailsSealed>{
        viewModelScope.launch {
            try{
                val isFavor = roomCash.isFavoriteCoroutine(state)
                isFavorite.value =DetailsSealed.Success(isFavorite = isFavor)
            }catch (error:Exception){
                isFavorite.value =DetailsSealed.Error(error = error)
                Log.d(TAG, "DetailsViewModel isFavoriteStateCoroutine error = ${error.message} ")
            }
        }
        return isFavorite
    }

    fun addToFavoriteCoroutine(state: State){
        viewModelScope.launch {
            try{
                roomCash.addToFavoriteCoroutine(state)
                isFavorite.value = DetailsSealed.Success(isFavorite = true)
            }catch (error:Exception){
                isFavorite.value =DetailsSealed.Error(error = error)
                Log.d(TAG, "DetailsViewModel addToFavoriteCoroutine error = ${error.message} ")
            }
        }
    }

    fun removeFavoriteCoroutine(state: State){
        viewModelScope.launch {
            try{
                roomCash.removeFavoriteCoroutine(state)
                isFavorite.value = DetailsSealed.Success(isFavorite = false)
            }catch (error:Exception){
                isFavorite.value =DetailsSealed.Error(error = error)
                Log.d(TAG, "DetailsViewModel removeFavorite error = ${error.message} ")
            }
        }
    }

    fun getRusLang():Boolean{
        return helper.getRusLang()
    }

}