package com.bartex.statesmvvm.model.repositories.states.cash

import android.util.Log
import androidx.room.Room
import com.bartex.statesmvvm.App
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

class RoomStateCash(val db: Database): IRoomStateCash {

    companion object{
        const val TAG = "33333"
    }

    override fun doStatesCash(listStates: List<State>): Single<List<State>> {
     return  Single.fromCallable { //создаём  Single из списка, по пути пишем в базу
         // map для базы, так как классы разные
         val roomUsers = listStates.map {state->
             Log.d(TAG, "RoomStateCash doStatesCash: state.nameRus = ${MapOfState.mapStates[state.name] }")
             RoomState(
                 state.capital ?: "",
                 state.flag ?: "",
                 state.name ?: "",
                 state.region ?: "",
                 state.population ?: 0,
                 state.area?:0f,
                 state.latlng?.get(0) ?:0f,
                 state.latlng?.get(1) ?:0f,
                 MapOfState.mapStates[state.name] ?:"Unknown",
                 MapOfCapital.mapCapital[state.capital] ?:"Unknown",
                 MapOfRegion.mapRegion[state.region] ?:"Unknown"
             )
         }
           db.stateDao.insert(roomUsers) //пишем в базу
           Log.d(TAG, "RoomStateCash doStatesCash: size = ${db.stateDao.getAll().size}")
           return@fromCallable listStates //возвращаем toolbar_menu  в виде Single<List<State>>
       }
    }

    override fun getStatesFromCash(): Single<List<State>> {
        Log.d(TAG, "RoomStateCash getStatesFromCash")
        return  Single.fromCallable {
          db.stateDao.getAll().map {roomState->
              State(roomState.capital,roomState.flag, roomState.name, roomState.region,
                  roomState.population, roomState.area, arrayOf(roomState.lat, roomState.lng),
                  roomState.nameRus, roomState.capitalRus, roomState.regionRus
              )
          }
        }
    }

    override fun getSearchedStatesFromCash(search: String) : Single<List<State>> =Single.fromCallable {
        Log.d(TAG, "RoomStateCash getSearchedStatesFromCash: search = $search")
        db.stateDao.findByName(search).map {roomState->
                State(roomState.capital,roomState.flag, roomState.name, roomState.region,
                    roomState.population, roomState.area, arrayOf(roomState.lat, roomState.lng),
                    roomState.nameRus, roomState.capitalRus, roomState.regionRus
                )
            }
        }.subscribeOn(Schedulers.io())

    override fun getSearchedStatesFromCashRus(search: String) : Single<List<State>> =
        Single.fromCallable {
            Log.d(TAG, "RoomStateCash getSearchedStatesFromCashRus: search = $search")
            db.stateDao.findByNameRus(search).map {roomState->
                State(roomState.capital,roomState.flag, roomState.name, roomState.region,
                    roomState.population, roomState.area, arrayOf(roomState.lat, roomState.lng),
                    roomState.nameRus, roomState.capitalRus, roomState.regionRus
                )
            }
        }.subscribeOn(Schedulers.io())

    override fun loadFavorite(): Single<List<State>> = Single.fromCallable {
            db.favoriteDao.getAll().map {roomFavorite->
                State(roomFavorite.capital,roomFavorite.flag, roomFavorite.name,
                    roomFavorite.region,roomFavorite.population, roomFavorite.area,
                    arrayOf(roomFavorite.lat, roomFavorite.lng), roomFavorite.nameRus,
                    roomFavorite.capitalRus, roomFavorite.regionRus)
            }
        }.subscribeOn(Schedulers.io())


    override fun isFavorite(state: State):Single<Boolean> {
        return Single.fromCallable {
            var rf: RoomFavorite? = null
            state.name?.let {
                rf = db.favoriteDao.findByName(it)
            }
            return@fromCallable rf!=null
        }
    }

    override fun removeFavorite(state: State): Completable = Completable.create { emitter->
        removeFavor(state). let{
            if(it){
                emitter.onComplete()
                Log.d(TAG, "RoomStateCash addToFavorite emitter.onComplete()")
            }else{
                emitter.onError(RuntimeException(" Ошибка при удалении из избранного "))
            }
        }
    }.subscribeOn(Schedulers.io())

    private fun removeFavor(state: State): Boolean{
        var roomFavorite:RoomFavorite? = null
        state.name?. let{roomFavorite = db.favoriteDao.findByName(it)}
        roomFavorite?. let{
            db.favoriteDao.delete(it)
            return true
        } ?:return false
    }


    override fun addToFavorite(state: State): Completable = Completable.create { emitter->
        add(state). let{
            if(it){
                emitter.onComplete()
                Log.d(TAG, "RoomStateCash addToFavorite emitter.onComplete()")
            }else{
                emitter.onError(RuntimeException(" Ошибка при добавлении в избранное "))
            }
        }
    }.subscribeOn(Schedulers.io())

    private fun add(state: State):Boolean {
        val roomFavorite = RoomFavorite(
            capital = state.capital ?: "",
            flag = state.flag ?: "",
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
        state.name?. let{
         val rs:RoomFavorite? =   db.favoriteDao.findByName(it)
            rs?: let{
                db.favoriteDao.insert(roomFavorite)
                return true
            }
        }
       return false
    }
}