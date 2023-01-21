package com.bartex.statesmvvm.model.repositories.states

import com.bartex.statesmvvm.common.MapOfCapital
import com.bartex.statesmvvm.common.MapOfRegion
import com.bartex.statesmvvm.common.MapOfState
import com.bartex.statesmvvm.model.api.DataSourceRetrofit
import com.bartex.statesmvvm.model.api.IDataSource
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.model.repositories.states.cash.RoomStateCash
import com.bartex.statesmvvm.model.room.Database
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

//Получаем интерфейс через конструктор и пользуемся им для получения стран.
// Обратите внимание, что поток, на который мы подписываемся, указан именно тут.
// Таким образом, мы позволяем репозиторию самостоятельно следить за тем, чтобы сетевые вызовы
// выполнялись именно в io-потоке. Всегда лучше поступать именно таким образом, даже когда речь
// не идёт о сети — во избежание выполнения операций в неверном потоке в вызывающем коде.
class StatesRepo(
    private val dataSource: IDataSource,
    private val roomCash: IRoomStateCash )
    : IStatesRepo {

    companion object{
        const val TAG = "33333"
    }
        // сюда попадаем только если в базе ничего нет - эта развилка реализована в onViewCreated StatesFragment
    override fun getStates(): Single<List<State>> =
            dataSource.getStates() //получаем данные из сети в виде Single<List<State>>
                .flatMap {states->//получаем доступ к списку List<State>
                    //добавляем русские названия из Map в поля State
                    states.map {
                        it.nameRus = MapOfState.mapStates[it.name] ?:"Unknown"
                        it.capitalRus = MapOfCapital.mapCapital[it.capital] ?:"Unknown"
                        it.regionRus = MapOfRegion.mapRegion[it.region] ?:"Unknown"
                    }
                    //реализация кэширования списка пользователей из сети в базу данных
                    roomCash.doStatesCash(states)
        }.subscribeOn(Schedulers.io())

    override suspend fun getStatesCoroutine(): List<State> {
        val listStates = dataSource.getStatesCoroutine()
        listStates.forEach {
            it.nameRus = MapOfState.mapStates[it.name] ?:"Unknown"
            it.capitalRus = MapOfCapital.mapCapital[it.capital] ?:"Unknown"
            it.regionRus = MapOfRegion.mapRegion[it.region] ?:"Unknown"
        }
        roomCash.doStatesCashCoroutine(listStates)
        return listStates
    }

}