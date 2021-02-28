package com.bartex.statesmvvm.model.repositories.states.cash

import android.util.Log
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.room.Database
import com.bartex.statesmvvm.model.room.tables.RoomFavorite
import com.bartex.statesmvvm.model.room.tables.RoomState
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class RoomStateCash(val db: Database): IRoomStateCash {

    companion object{
        const val TAG = "33333"
    }

    override fun doStatesCash(listStates: List<State>): Single<List<State>> {
     return  Single.fromCallable { //создаём  Single из списка, по пути пишем в базу
         // map для базы, так как классы разные
         val roomUsers = listStates.map {state->
             RoomState(
                 state.capital ?: "",
                 state.flag ?: "",
                 state.name ?: "",
                 state.region ?: "",
                 state.population ?: 0,
                 state.area?:0f,
                 state.latlng?.get(0) ?:0f,
                 state.latlng?.get(1) ?:0f
             )
         }
           db.stateDao.insert(roomUsers) //пишем в базу
           Log.d(TAG, "RoomStateCash doStatesCash: roomUsers.size = ${roomUsers.size}")
           return@fromCallable listStates //возвращаем toolbar_menu  в виде Single<List<State>>
       }
    }

    override fun getStatesFromCash(): Single<List<State>> {
      return  Single.fromCallable {
          db.stateDao.getAll().map {roomState->
              State(roomState.capital,roomState.flag, roomState.name, roomState.region,
                  roomState.population, roomState.area, arrayOf(roomState.lat, roomState.lng))
          }
        }
    }

    override fun getSearchedStatesFromCash(search: String) : Single<List<State>>{
        return  Single.fromCallable {
            db.stateDao.findByName(search).map {roomState->
                State(roomState.capital,roomState.flag, roomState.name, roomState.region,
                    roomState.population, roomState.area, arrayOf(roomState.lat, roomState.lng))
            }
        }
    }


    override fun loadFavorite(): Single<List<State>> {
        Log.d(TAG, "RoomStateCash loadFavorite")
        return  Single.fromCallable {
            db.favoriteDao.getAll().map {roomFavorite->
                State(roomFavorite.capital,roomFavorite.flag, roomFavorite.name,
                    roomFavorite.region,roomFavorite.population, roomFavorite.area,
                    arrayOf(roomFavorite.lat, roomFavorite.lng))
            }
        }
    }

    override fun isFavorite(state: State):Single<Boolean> {
        return Single.fromCallable {
            var rf: RoomFavorite? = null
            state.name?.let {
                rf = db.favoriteDao.findByName(it)
            }
            return@fromCallable rf!=null
        }
    }

    override fun removeFavorite(state: State)= Completable.create {emitter->
        removeFavor(state). let{
            if(it){
                emitter.onComplete()
                Log.d(TAG, "RoomStateCash addToFavorite emitter.onComplete()")
            }else{
                emitter.onError(RuntimeException(" Ошибка при добавлении в избранное "))
            }
        }
    }

    private fun removeFavor(state: State): Boolean{
        var roomFavorite:RoomFavorite? = null
        state.name?. let{roomFavorite = db.favoriteDao.findByName(it)}
        roomFavorite?. let{
            db.favoriteDao.delete(it)
            return true
        } ?:return false
    }


    override fun addToFavorite(state: State)= Completable.create {emitter->
        add(state). let{
            if(it){
                emitter.onComplete()
                Log.d(TAG, "RoomStateCash addToFavorite emitter.onComplete()")
            }else{
                emitter.onError(RuntimeException(" Ошибка при добавлении в избранное "))
            }
        }
    }

    private fun add(state: State):Boolean {
        val roomFavorite = RoomFavorite(
            capital = state.capital ?: "",
            flag = state.flag ?: "",
            name = state.name ?: "",
            region =  state.region ?: "",
            population = state.population ?: 0,
            area =  state.area?:0f,
            lat = state.latlng?.get(0) ?:0f,
            lng =  state.latlng?.get(1) ?:0f
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