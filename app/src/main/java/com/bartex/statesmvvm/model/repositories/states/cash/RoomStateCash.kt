package com.bartex.statesmvvm.model.repositories.states.cash

import android.util.Log
import androidx.lifecycle.LiveData
import com.bartex.statesmvvm.common.MapOfCapital
import com.bartex.statesmvvm.common.MapOfRegion
import com.bartex.statesmvvm.common.MapOfState
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.room.Database
import com.bartex.statesmvvm.model.room.tables.RoomFavorite
import com.bartex.statesmvvm.model.room.tables.RoomState
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class RoomStateCash(private val db: Database ):IRoomStateCash {

    companion object{
        const val TAG = "33333"
    }

    override suspend fun doStatesCashCoroutine(listStates: List<State>) {
        val litRoomStates:MutableList<RoomState> = mutableListOf()
        listStates.forEach {state->
           val roomState =  RoomState(
                state.capital ?: "",
                state.flag?: "",
                state.name ?: "",
                state.region ?: "",
                state.population ?: 0,
                state.area?:0f,
                state.latlng?.get(0) ?:0f,
                state.latlng?.get(1) ?:0f,
                state.nameRus?:"Unknown",
                state.capitalRus?:"Unknown",
                state.regionRus ?:"Unknown"
            )
            litRoomStates.add(roomState)
        }
        db.stateDao.deleteAll() //стираем предыдущий список
        db.stateDao.insertList(litRoomStates) //пишем в базу
    }


    override suspend fun loadFavoriteCoroutine(): List<State> {
       return db.favoriteDao.getAllCoroutine().map {roomFavorite->
            State(roomFavorite.capital, roomFavorite.flag, roomFavorite.name,
                roomFavorite.region,roomFavorite.population, roomFavorite.area,
                arrayOf(roomFavorite.lat, roomFavorite.lng), roomFavorite.nameRus,
                roomFavorite.capitalRus, roomFavorite.regionRus)
        }
    }

    //получение списка для флагов
    override suspend fun getFlagsFromCashCoroutine(): List<State> {
       return db.stateDao.getAllCoroutine().map { roomState->
            State(roomState.capital, roomState.flag, roomState.name, roomState.region,
                roomState.population, roomState.area, arrayOf(roomState.lat, roomState.lng),
                roomState.nameRus, roomState.capitalRus, roomState.regionRus
            )
        }
    }

    override suspend fun writeMistakeInDatabaseCoroutine(mistakeAnswer: String): Boolean {
        val mistakeRoomState: RoomState = db.stateDao.getStateByNameRusCoroutine(mistakeAnswer) //получаем страну по имени
        if (mistakeRoomState.mistake == 0) { //если статус ошибки = 0
            mistakeRoomState.mistake = 1 //меняем статус ошибки на 1
            db.stateDao.updateCoroutine(mistakeRoomState) //обновляем запись в базе
        }
        //проверяем как прошла запись
        val result:Int =  db.stateDao.getMistakeByNameRusCoroutine(mistakeAnswer)
        return result == 1 //если 1 - возвращаем true, иначе false
    }

    override fun getAllMistakesLive(): LiveData<List<RoomState>> {
        return db.stateDao.getAllMistakesLive()
    }

    override suspend fun removeMistakeFromDatabaseCoroutine(nameRus: String): Boolean {
        val mistakeRoomState: RoomState = db.stateDao.getStateByNameRusCoroutine(nameRus) //получаем страну по имени
        if (mistakeRoomState.mistake == 1) { //если статус ошибки = 0
            mistakeRoomState.mistake = 0 //меняем статус ошибки на 1
            db.stateDao.updateCoroutine(mistakeRoomState) //обновляем запись в базе
        }
        //проверяем как прошло удаление отметки
        val result:Int =  db.stateDao.getMistakeByNameRusCoroutine(nameRus)
        return result == 0 //если 0 - возвращаем true, иначе false
    }

    override fun getAllDataLive(): LiveData<List<RoomState>> {
        return db.stateDao.getAllRegionsLive()
    }

    override suspend fun isFavoriteCoroutine(state: State): Boolean {
        var rf: RoomFavorite? = null
        state.name?.let {
            rf = db.favoriteDao.findByNameCoroutine(it)
        }
        return rf!=null
    }

    override suspend fun loadAllDataCoroutine(): List<State> {
       return db.stateDao.getAllCoroutine().map{roomState->
            State(roomState.capital, roomState.flag, roomState.name, roomState.region,
                roomState.population, roomState.area, arrayOf(roomState.lat, roomState.lng),
                roomState.nameRus, roomState.capitalRus, roomState.regionRus
            )
        }
    }

    override suspend fun removeFavoriteCoroutine(state: State) {
        var roomFavorite:RoomFavorite? = null
        state.name?. let{roomFavorite = db.favoriteDao.findByNameCoroutine(it)}
        roomFavorite?. let{
            db.favoriteDao.deleteCoroutine(it)
        }
    }

    override suspend fun addToFavoriteCoroutine(state: State) {
        val roomFavorite = RoomFavorite(
            capital = state.capital ?: "",
            flag = state.flag?: "",
            name = state.name ?: "",
            region =  state.region ?: "",
            population = state.population ?: 0,
            area =  state.area?:0f,
            lat = state.latlng?.get(0) ?:0f,
            lng =  state.latlng?.get(1) ?:0f,
            nameRus = state.nameRus?:"Unknown",
            capitalRus = state.capitalRus?:"Unknown",
            regionRus =  state.regionRus?:"Unknown"
        )
        var rs: RoomFavorite? = null
        state.name?. let{ rs = db.favoriteDao.findByNameCoroutine(it) }
        rs?. let{
            Log.d(TAG, "RoomStateCash addToFavoriteCoroutine  ")
        }?:db.favoriteDao.insertCoroutine(roomFavorite)
    }

}