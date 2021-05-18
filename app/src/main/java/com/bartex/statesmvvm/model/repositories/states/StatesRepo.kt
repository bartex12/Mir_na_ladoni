package com.bartex.statesmvvm.model.repositories.states

import android.util.Log
import com.bartex.statesmvvm.common.MapOfCapital
import com.bartex.statesmvvm.common.MapOfRegion
import com.bartex.statesmvvm.common.MapOfState
import com.bartex.statesmvvm.model.api.IDataSourceState
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.network.INetworkStatus
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.view.fragments.search.SearchViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

//Получаем интерфейс через конструктор и пользуемся им для получения стран.
// Обратите внимание, что поток, на который мы подписываемся, указан именно тут.
// Таким образом, мы позволяем репозиторию самостоятельно следить за тем, чтобы сетевые вызовы
// выполнялись именно в io-потоке. Всегда лучше поступать именно таким образом, даже когда речь
// не идёт о сети — во избежание выполнения операций в неверном потоке в вызывающем коде.
class StatesRepo(val api: IDataSourceState, private val networkStatus: INetworkStatus,
                 private val roomCash: IRoomStateCash):    IStatesRepo {

    companion object{
        const val TAG = "33333"
    }
    //метод  интерфейса IDataSourceState getStates() - в зависимости от статуса сети
    //мы или получаем данные из сети, записывая их в базу данных с помощью Room через map
    //или берём из базы, преобразуя их также через map
    override fun getStates(): Single<List<State>> =
        networkStatus.isOnlineSingle()
            .flatMap {isOnLine-> //получаем доступ к Boolean значениям
                if (isOnLine){ //если сеть есть
                    Log.d(TAG, "StatesRepo getStates  isOnLine  = true ")
                    api.getStates() //получаем данные из сети в виде Single<List<State>>
                        .flatMap {states->//получаем доступ к списку List<State>
                            //фильтруем данные
                           val f_states =  states.filter {state->
                                state.capital!=null &&  //только со столицами !=null
                                state.latlng?.size == 2 && //только с известными координатами
                                state.capital.isNotEmpty() //только с известными столицами
                            }
                            //добавляем русские названия из Map в поля State
                            states.map {
                                it.nameRus = MapOfState.mapStates[it.name] ?:"Unknown"
                                it.capitalRus = MapOfCapital.mapCapital[it.capital] ?:"Unknown"
                                it.regionRus = MapOfRegion.mapRegion[it.region] ?:"Unknown"
                            }
                            Log.d(TAG, "StatesRepo  getStates f_states.size = ${f_states.size}")
                            //реализация кэширования списка пользователей из сети в базу данных
                           roomCash.doStatesCash(f_states)
                          }
                }else{
                    Log.d(TAG, "StatesRepo  isOnLine  = false")
                    //получение списка пользователей из кэша
                    roomCash.getStatesFromCash()
                }
            }.subscribeOn(Schedulers.io())

    override fun searchStatesFromRoomRus(search: String): Single<List<State>> {
        Log.d(TAG, "StatesRepo searchStatesFromRoomRus search = $search")
       return roomCash.getSearchedStatesFromCashRus(search)
    }
    override fun searchStatesFromRoomEng(search: String): Single<List<State>> {
        Log.d(TAG, "StatesRepo searchStatesFromRoomEng search = $search")
        return roomCash.getSearchedStatesFromCashEng(search)
    }
}