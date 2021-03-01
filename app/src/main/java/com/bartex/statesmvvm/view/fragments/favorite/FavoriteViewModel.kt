package com.bartex.statesmvvm.view.fragments.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class FavoriteViewModel() : ViewModel() {

    companion object{
        const val TAG = "33333"
    }

    @Inject
    lateinit var roomCash: IRoomStateCash

    private  var listStates = MutableLiveData<List<State>>()

    fun getFavorite():LiveData<List<State>> = listStates

    fun loadFavorite() {
        roomCash. loadFavorite()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({states->
                //обновляем список в случае его изменения
                Log.d(TAG, "FavoriteViewModel loadFavorite: states.size =  ${states.size}")
                listStates.value = states
            }, {error ->
                Log.d(TAG, "FavoriteViewModel onError ${error.message}")
        })
    }
}