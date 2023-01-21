package com.bartex.statesmvvm.view.fragments.flags

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.view.fragments.scheduler.SchedulerProvider
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class FlagViewModel(
   private val helper : IPreferenceHelper,
   private val roomCash: IRoomStateCash,
   private val schedulerProvider: SchedulerProvider
):ViewModel() {

    companion object{
        const val TAG = "33333"
    }

    private  var listFlags = MutableLiveData<List<State>>()

    fun getStateFlafs(): LiveData<List<State>> {
        getStatesFromCash()
        return listFlags
    }

    private fun getStatesFromCash() {
        val isSorted = helper.isSorted()
        val getSortCase = helper.getSortCase()
        var f_st:List<State> = listOf()
        roomCash.getFlagsFromCash()
            .observeOn(Schedulers.computation())
            .flatMap {st->
                if(isSorted){
                    when (getSortCase) {
                        1 -> {f_st = st.filter {it.population!=null}.sortedByDescending {it.population}}
                        2 -> {f_st = st.filter {it.population!=null}.sortedBy {it.population}}
                        3 -> {f_st = st.filter {it.area!=null}.sortedByDescending {it.area} }
                        4 -> { f_st = st.filter {it.area!=null}.sortedBy {it.area} }
                        5 -> {f_st = st.filter {it.population!=null && it.population >0 && it.area!=null && it.area!! >0}
                            .sortedByDescending {it.population!!.toFloat()/it.area!!}}
                        6 -> {f_st = st.filter {it.population!=null && it.population >0 && it.area!=null && it.area!! >0}
                            .sortedBy {it.population!!.toFloat()/it.area!!}}
                    }
                    return@flatMap Single.just(f_st)
                }else{
                    return@flatMap Single.just( st.sortedBy{it.name})
                }
            }
            .observeOn(schedulerProvider.ui())
            .subscribe ({states->
                //обновляем список в случае его изменения
                states.let{
                    Log.d(TAG, "FlagViewModel loadFavorite: states.size =  ${it.size}")}
                listFlags.value = states
            }, {error ->
                Log.d(TAG, "FlagViewModel onError ${error.message}")
            })
    }
}