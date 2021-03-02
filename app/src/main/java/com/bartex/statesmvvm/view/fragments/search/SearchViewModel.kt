package com.bartex.statesmvvm.view.fragments.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.IStatesRepo
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel: ViewModel() {

    companion object{
        const val TAG = "33333"
    }

    @Inject
    lateinit var helper : IPreferenceHelper
    @Inject
    lateinit var statesRepo: IStatesRepo
    @Inject
    lateinit var mainThreadScheduler: Scheduler

    val listStatesSearch = MutableLiveData<List<State>>()

    fun getStatesSearch() :LiveData<List<State>> = listStatesSearch

    fun loadDataSearch(search:String?){
        val isSorted = helper.isSorted()
        val getSortCase = helper.getSortCase()
        var f_st:List<State>?= null
        search?. let{
            statesRepo.searchStates(search)
                .observeOn(Schedulers.computation())
                .flatMap {st->
                    if(isSorted){
                        when (getSortCase) {
                            1 -> {f_st = st.filter {it.population!=null}.sortedByDescending {it.population} }
                            2 -> {f_st = st.filter {it.population!=null}.sortedBy {it.population} }
                            3 -> {f_st = st.filter {it.area!=null}.sortedByDescending {it.area}}
                            4 -> {f_st = st.filter {it.area!=null}.sortedBy {it.area}}
                        }
                        return@flatMap Single.just(f_st)
                    }else{
                        return@flatMap Single.just(st)
                    }
                }
                .observeOn(mainThreadScheduler)
                .subscribe ({states->
                    states?. let{
                        Log.d(TAG, "SearchViewModel  loadData states.size = ${it.size}")
                        listStatesSearch.value = it
                    }
                }, {error -> Log.d(TAG, "SearchViewModel onError ${error.message}")
                })
        }?: Single.just(listOf<State>())
    }

    fun getPositionSearch(): Int{
        return helper.getPositionSearch()
    }

    fun savePositionSearch(firstPosition:Int){
        return helper.savePositionSearch(firstPosition)
    }
}
