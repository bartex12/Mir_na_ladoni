package com.bartex.statesmvvm.view.fragments.quiz.flagstatemistake

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.model.room.tables.RoomState
import kotlinx.coroutines.launch

class MistakesQuizModel(
    private var helper : IPreferenceHelper,
    private var roomCash : IRoomStateCash
):ViewModel() {

    fun getPositionState(): Int{
        return helper.getPositionState()
    }

    fun savePositionState(position: Int){
        helper.savePositionState(position)
    }

    //получаем из базы сразу LiveData - без корутин и Flow
  fun  getAllMistakesLive():LiveData<List<RoomState>>{
    return  roomCash.getAllMistakesLive()
  }

    fun removeMistakeFromDatabaseCoroutine(nameRus: String ){
        viewModelScope.launch {
            try {
                val isMistakeRemoved =   roomCash.removeMistakeFromDatabaseCoroutine(nameRus)
                if(isMistakeRemoved){
                    Log.d(TAG, "MistakeRemoved: ")
                }else{
                    Log.d(TAG, "NOT Removed ")
                }
            }catch (error:Exception){
                Log.d(TAG, "${error.message}")
            }
        }
    }

    companion object{
        const val TAG = "Quizday"
    }
}