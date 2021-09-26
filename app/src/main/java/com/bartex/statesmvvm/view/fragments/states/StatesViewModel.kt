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
import com.bartex.statesmvvm.model.repositories.states.cash.RoomStateCash
import com.bartex.statesmvvm.model.room.Database
import com.bartex.statesmvvm.view.fragments.scheduler.SchedulerProvider
import com.bartex.statesmvvm.view.fragments.scheduler.StatesSchedulerProvider
import io.reactivex.rxjava3.core.Single

//var mainThreadScheduler:SchedulerProvider - сделан через интерфейс для целей тестирования
//при тестировании вместо StatesSchedulerProvider будет использован класс-заглушка ScheduleProviderStub
class StatesViewModel(
    var helper : IPreferenceHelper = PreferenceHelper(App.instance),
    var schedulerProvider: SchedulerProvider = StatesSchedulerProvider(),
    var statesRepo: IStatesRepo =StatesRepo(
        dataSource = DataSourceRetrofit(),
        roomCash =  RoomStateCash(db = Database.getInstance() as Database)
     )
): ViewModel() {

    companion object{
        const val TAG = "33333"
    }

   // @Inject
   // lateinit var helper : IPreferenceHelper
//    @Inject
//    lateinit var mainThreadScheduler: Scheduler
//    @Inject
//    lateinit var statesRepo: IStatesRepo  //репозиторий

    private val listStates = MutableLiveData<StatesSealed>()

    fun getStatesSealed() : LiveData<StatesSealed>{
        return listStates
    }

    fun loadDataSealed(isNetworkAvailable:Boolean){
        //начинаем загрузку данных
        listStates.value = StatesSealed.Loading(null)

        val isSorted = helper.isSorted()
        val getSortCase = helper.getSortCase()
        var f_st:List<State>?= null
        Log.d(TAG, "BasePresenter  loadData isSorted = $isSorted getSortCase = $getSortCase")

        statesRepo.getStates(isNetworkAvailable)
            .observeOn(schedulerProvider.computation())
            .flatMap {st->
                Log.d(TAG, "1 StatesViewModel  loadData st.size = ${st.size}")
                if(isSorted){
                    when (getSortCase) {
                        1 -> {f_st = st.filter {it.population!=null}.sortedByDescending {it.population} }
                        2 -> {f_st = st.filter {it.population!=null}.sortedBy {it.population} }
                        3 -> {f_st = st.filter {it.area!=null}.sortedByDescending {it.area}}
                        4 -> {f_st = st.filter {it.area!=null}.sortedBy {it.area}}
                    }
                    return@flatMap Single.just(f_st)
                }else{
                    Log.d(TAG, "2 StatesViewModel  loadData st.size = ${st.size}")
                    return@flatMap Single.just(st)
                }
            }
            .observeOn(schedulerProvider.ui())
            .subscribe ({states->
                states?. let{
                    // если данные загружены - выставляем value в MutableLiveData
                    listStates.value = StatesSealed.Success(state = it)
                    Log.d(TAG, "StatesViewModel  loadData states.size = ${it.size}")
                }
            }, {error ->
                //если произошла ошибка - выставляем value в MutableLiveData в ошибку
                listStates.value = StatesSealed.Error(error = error)
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
}