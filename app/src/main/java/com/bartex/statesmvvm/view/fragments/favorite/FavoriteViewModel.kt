package com.bartex.statesmvvm.view.fragments.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.model.utils.IStateUtils
import com.bartex.statesmvvm.model.utils.StateUtils
import com.bartex.statesmvvm.view.fragments.scheduler.SchedulerProvider
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class FavoriteViewModel(
    private val helper : IPreferenceHelper,
    private val roomCash: IRoomStateCash,
    private val schedulerProvider: SchedulerProvider,
    private val stateUtils: IStateUtils = StateUtils()
) : ViewModel() {

    companion object{
        const val TAG = "33333"
    }

    private  var listFavoriteStates = MutableLiveData<List<State>>()

    fun getFavorite():LiveData<List<State>>{
        loadFavorite()
        return listFavoriteStates
    }

    private fun loadFavorite() {
        val isSorted = helper.isSorted()
        val getSortCase = helper.getSortCase()
        var filterState:List<State> = listOf()
        roomCash. loadFavorite()
            .observeOn(Schedulers.computation())
            .flatMap {st->
                if(isSorted){
                    when (getSortCase) {
                        1 -> {filterState = st.filter {it.population!=null}.sortedByDescending {it.population}}
                        2 -> {filterState = st.filter {it.population!=null}.sortedBy {it.population}}
                        3 -> {filterState = st.filter {it.area!=null}.sortedByDescending {it.area} }
                        4 -> { filterState = st.filter {it.area!=null}.sortedBy {it.area} }
                        5 -> {filterState = st.filter {it.population!=null && it.population >0 && it.area!=null && it.area!! >0}
                            .sortedByDescending {it.population!!.toFloat()/it.area!!}}
                        6 -> {filterState = st.filter {it.population!=null && it.population >0 && it.area!=null && it.area!! >0}
                            .sortedBy {it.population!!.toFloat()/it.area!!}}
                    }
                    return@flatMap Single.just(filterState)
                }else{
                    return@flatMap Single.just( st.sortedBy{it.name})
                }
            }
            .observeOn(schedulerProvider.ui())
            .subscribe ({states->
                //обновляем список в случае его изменения
                states.let{
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

    fun getArea(area:Float?) = stateUtils.getStateArea(area)
    fun  getPopulation(population: Int?) = stateUtils.getStatePopulation(population)
    fun getDensity(area: Float?, population: Int?): String = stateUtils.getStateDensity(area, population)

    fun getRusLang():Boolean{
        return helper.getRusLang()
    }


}