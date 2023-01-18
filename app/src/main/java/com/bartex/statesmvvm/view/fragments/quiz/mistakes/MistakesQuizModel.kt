package com.bartex.statesmvvm.view.fragments.quiz.mistakes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.prefs.PreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.model.repositories.states.cash.RoomStateCash
import com.bartex.statesmvvm.model.room.Database
import com.bartex.statesmvvm.model.room.tables.RoomState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

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

  fun  getAllMistakesLive():LiveData<List<RoomState>>{
    return  roomCash.getAllMistakesLive()
  }

    fun removeMistakeFromDatabase(nameRus: String ){
            roomCash.removeMistakeFromDatabase(nameRus)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ isMistakeRemoved ->
                        if (isMistakeRemoved) {
                            Log.d(TAG, "MistakeRemoved: ")
                        }else{
                            Log.d(TAG, "NOT Removed ")
                        }
                    }, {error->
                        Log.d(TAG, "${error.message}")
                    })
    }

    companion object{
        const val TAG = "Quizday"
    }
}