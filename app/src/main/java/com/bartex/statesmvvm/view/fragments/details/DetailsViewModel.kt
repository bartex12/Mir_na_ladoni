package com.bartex.statesmvvm.view.fragments.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.model.utils.IStateUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DetailsViewModel:ViewModel() {
    companion object{
        const val TAG = "33333"
    }

    private val isFavorite = MutableLiveData<DetailsSealed>()

    @Inject
    lateinit var roomCash: IRoomStateCash
    @Inject
    lateinit var stateUtils: IStateUtils

    fun  getStateArea(state:State) = stateUtils.getStateArea(state)

    fun getStatePopulation(state:State) = stateUtils.getStatePopulation(state)

    fun getStateCapital(state:State) = stateUtils.getStateCapital(state)

    fun getStateRegion(state:State) = stateUtils.getStateRegion(state)

    fun getStateZoom(state:State) = stateUtils.getStatezoom(state)

    fun isFavoriteState(state: State):LiveData<DetailsSealed>{
        roomCash.isFavorite(state)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isFavorite.value =DetailsSealed.Success(isFavorite = it)
            }, {
                isFavorite.value =DetailsSealed.Error(error = it)
                Log.d(TAG, "DetailsViewModel isFavorite error = ${it.message} ")
            })
        return isFavorite
    }

    fun addToFavorite(state:State){
        Log.d(TAG, "DetailsViewModel addToFavorite ")
        roomCash.addToFavorite(state)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (   {
                isFavorite.value = DetailsSealed.Success(isFavorite = true)
            },{
                isFavorite.value =DetailsSealed.Error(error = it)
                Log.d(TAG, "DetailsViewModel addToFavorite error = ${it.message} ")
            })
    }

    fun removeFavorite(state:State){
        Log.d(TAG, "DetailsViewModel removeFavorite ")
        roomCash.removeFavorite(state)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (   {
                isFavorite.value = DetailsSealed.Success(isFavorite = false)
            },{
                isFavorite.value =DetailsSealed.Error(error = it)
                Log.d(TAG, "DetailsViewModel removeFavorite error = ${it.message} ")
            })
    }

}