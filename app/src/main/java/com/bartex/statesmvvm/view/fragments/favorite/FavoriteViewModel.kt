package com.bartex.statesmvvm.view.fragments.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.model.utils.IStateUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class FavoriteViewModel() : ViewModel() {

    companion object{
        const val TAG = "33333"
    }

    @Inject
    lateinit var helper : IPreferenceHelper
    @Inject
    lateinit var roomCash: IRoomStateCash
    @Inject
    lateinit var mainThreadScheduler: Scheduler
    @Inject
    lateinit var stateUtils: IStateUtils

    private  var listFavoriteStates = MutableLiveData<List<State>>()

    fun getFavorite():LiveData<List<State>>{
        loadFavorite()
        return listFavoriteStates
    }

    private fun loadFavorite() {
        val isSorted = helper.isSorted()
        val getSortCase = helper.getSortCase()
        var f_st:List<State>?= null
        roomCash. loadFavorite()
            .observeOn(Schedulers.computation())
            .flatMap {st->
                if(isSorted){
                    when (getSortCase) {
                        1 -> {f_st = st.filter {it.population!=null}.sortedByDescending {it.population}}
                        2 -> {f_st = st.filter {it.population!=null}.sortedBy {it.population}}
                        3 -> {f_st = st.filter {it.area!=null}.sortedByDescending {it.area} }
                        4 -> { f_st = st.filter {it.area!=null}.sortedBy {it.area} }
                    }
                    return@flatMap Single.just(f_st)
                }else{
                    return@flatMap Single.just(st)
                }

            }
            .subscribeOn(Schedulers.io())
            .observeOn(mainThreadScheduler)
            .subscribe ({states->
                //обновляем список в случае его изменения
                states?. let{
                    Log.d(TAG, "FavoriteViewModel loadFavorite: states.size =  ${it.size}")}
                    listFavoriteStates.value = states
            }, {error ->
                Log.d(TAG, "FavoriteViewModel onError ${error.message}")
        })
    }

    fun savePositionFavorite(position: Int){
        helper.savePositionFavorite(position)
    }

    fun getPositionFavorite(): Int{
        return helper.getPositionFavorite()
    }

    fun getArea(state:State) = stateUtils.getStateArea(state)

    fun  getPopulation(state:State) = stateUtils.getStatePopulation(state)
}