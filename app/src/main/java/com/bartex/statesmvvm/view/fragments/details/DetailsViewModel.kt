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

    private val isFavorite = MutableLiveData<Boolean>()
    private val isAddToFavorite = MutableLiveData<Boolean>()
    private val isRemoveFavorite = MutableLiveData<Boolean>()

    fun isFavorite():LiveData<Boolean> = isFavorite
    fun isAddToFavorite():LiveData<Boolean> = isAddToFavorite
    fun isRemoveFavorite():LiveData<Boolean> = isRemoveFavorite

    @Inject
    lateinit var roomCash: IRoomStateCash
    @Inject
    lateinit var stateUtils: IStateUtils

    fun isFavoriteState(state: State):LiveData<Boolean>{
        roomCash.isFavorite(state)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isFavorite.value =true
            }, {
                isFavorite.value =false
                Log.d(TAG, "DetailsViewModel isFavorite error = ${it.message} ")
            })
        return isFavorite
    }

    fun  getStateArea(state:State) = stateUtils.getStateArea(state)

    fun getStatePopulation(state:State) = stateUtils.getStatePopulation(state)

    fun getStateCapital(state:State) = stateUtils.getStateCapital(state)

    fun getStateRegion(state:State) = stateUtils.getStateRegion(state)

    fun addToFavorite(state:State){
        Log.d(TAG, "DetailsViewModel addToFavorite ")
        roomCash.addToFavorite(state)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (   {
                isAddToFavorite.value = true
            },{
                isAddToFavorite.value = false
                Log.d(TAG, "DetailsViewModel addToFavorite error = ${it.message} ")
            })
    }

    fun removeFavorite(state:State){
        Log.d(TAG, "DetailsViewModel removeFavorite ")
        roomCash.removeFavorite(state)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (   {
                isRemoveFavorite.value = true
            },{
                isRemoveFavorite.value = false
                Log.d(TAG, "DetailsViewModel removeFavorite error = ${it.message} ")
            })
    }

  fun getStateZoom(state:State) = stateUtils.getStatezoom(state)
}