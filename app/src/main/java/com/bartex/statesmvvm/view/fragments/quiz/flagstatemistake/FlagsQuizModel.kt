package com.bartex.statesmvvm.view.fragments.quiz.flagstatemistake

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.states.IStatesRepo
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.view.fragments.quiz.fsm.Action
import com.bartex.statesmvvm.view.fragments.quiz.fsm.IFlagState
import com.bartex.statesmvvm.view.fragments.quiz.fsm.entity.Answer
import com.bartex.statesmvvm.view.fragments.quiz.fsm.entity.DataFlags
import com.bartex.statesmvvm.view.fragments.quiz.fsm.storage.IFlagQuiz
import com.bartex.statesmvvm.view.fragments.quiz.fsm.substates.ReadyState
import com.bartex.statesmvvm.view.fragments.quiz.setting.ISettingsProvider
import com.bartex.statesmvvm.view.fragments.states.StatesSealed
import com.bartex.statesmvvm.view.fragments.states.StatesViewModel
import com.bartex.statesmvvm.view.utils.UtilFilters
import kotlinx.coroutines.launch

class FlagsQuizModel(
    private var statesRepo: IStatesRepo,
    private val storage: IFlagQuiz,
    private val settingProvider: ISettingsProvider,
    private val roomCash: IRoomStateCash
) : ViewModel()  {

    //список стран из сети
    private val listStatesFromNet = MutableLiveData<StatesSealed>()
    //список стран из базы
    private val listStatesFromDatabase = MutableLiveData<List<State>>()
    //состояние конечного автомата
    val currentQuizState: MutableLiveData<IFlagState> = MutableLiveData<IFlagState>()

    var dataFlags: DataFlags = DataFlags() // здесь храним данные для состояний конечного автомата
    private var listOfStates:MutableList<State> = mutableListOf() //Здесь храним список стран из сети
    var region:String = Constants.REGION_EUROPE //Здесь храним текущий регион
    var currentState: IFlagState = ReadyState(DataFlags()) //Здесь храним текущее состояние
    private var isNeedToCreateDialog:Boolean = true//Здесь храним флаг необходимости создания диалога

    fun getNeedDialog():Boolean{
        return isNeedToCreateDialog
    }

    fun setNeedDialog(isNeed:Boolean){
        isNeedToCreateDialog = isNeed
    }

    fun getStatesSealed() : LiveData<StatesSealed> {
        loadDataSealed()
        return listStatesFromNet
    }

    private fun loadDataSealed(){
        listStatesFromNet.value = StatesSealed.Loading(0)
        viewModelScope.launch {
            try{
                val listStates = statesRepo.getStatesCoroutine()
                listStatesFromNet.value = StatesSealed.Success(state = listStates)
                Log.d(StatesViewModel.TAG, "StatesViewModel  loadDataSealed listStates.size = ${listStates.size}")
            }catch (error:Exception){
                listStatesFromNet.value = StatesSealed.Error(error = error)
                Log.d(StatesViewModel.TAG, "StatesViewModel onError ${error.message}")
            }
        }
    }

    //получить состояние конечного автомата
    fun getCurrentState(): LiveData<IFlagState> {
        return currentQuizState
    }

    //сохраняем список стран чтобы не пропадал при поворотах экрана
    fun saveListOfStates( listStates:MutableList<State>){
        val filteredListOfStates = listStates.filter {
            UtilFilters.filterData(it)
        }.toMutableList()
        dataFlags.listStatesFromNet = filteredListOfStates //для удобства храним в данных
        listOfStates = filteredListOfStates //а также храним во ViewModel
        Log.d(TAG, "BaseViewModel saveListOfStates  listOfStates size = ${listOfStates.size}")
    }

    fun getDataFromDatabase(): LiveData<List<State>> {
        loadDataFromDatabaseCoroutine()
        return listStatesFromDatabase
    }

    private fun loadDataFromDatabaseCoroutine() {
        viewModelScope.launch {
            try {
              val listStates =   roomCash.loadAllDataCoroutine()
                listStatesFromDatabase.value = listStates
            }catch (error:Exception){
                Log.d(TAG, "${error.message}")
            }
        }
    }

    //начальное состояние не имеет предыдущего
    fun resetQuiz(){
        setNeedDialog(true) //возвращаем флаг разрешения создания диалога
        dataFlags =  storage.resetQuiz(listOfStates, dataFlags, region) //подготовка переменных и списков
        currentQuizState.value =  ReadyState(dataFlags) //передаём полученные данные в состояние
    }

    //загрузить следующий флаг
    fun loadNextFlag(dataFlags: DataFlags){
        this.dataFlags =  storage.loadNextFlag(dataFlags)
        currentQuizState.value =  currentState.executeAction(Action.OnNextFlagClicked(this.dataFlags))
    }

    fun writeMistakeInDatabaseCoroutine() {
        Log.d(TAG, "###FlagsQuizModel writeMistakeInDatabaseCoroutine: ")
        viewModelScope.launch {
            try {
                dataFlags.correctAnswer?. let{
                    val isMistakeWrite =  roomCash.writeMistakeInDatabaseCoroutine(it)
                    if (isMistakeWrite) {
                        Log.d(TAG, "writeMistakeInDatabase ")
                    }else{
                        Log.d(TAG, "NOT write ")
                    }
                }
            }catch (error:Exception){
                Log.d(TAG, "${error.message}")
            }
        }
    }

    //обновить настройки звука
    fun updateSoundOnOff(){
        settingProvider.updateSoundOnOff()
    }

    //обновить количество вопросов в викторине
    fun updateNumberFlagsInQuiz(){
        dataFlags = settingProvider.updateNumberFlagsInQuiz(dataFlags)
    }

    //получить количество рядов кнопок с ответами
    fun getGuessRows():Int{
        dataFlags = settingProvider.getGuessRows(dataFlags)
        return dataFlags.guessRows
    }

    //сохраняем текущее состояние
    fun saveCurrentState( newState: IFlagState){
        currentState = newState
    }

    //сохраняем регион в классе данных
    fun saveRegion( newRegion:String){
        dataFlags.region = newRegion // в dataFlags
        region = newRegion  // в переменную ViewModel
    }

    fun getCurrentRegion( ):String{
        return  region
    }

    fun getRegionNameAndNumber( data: DataFlags):String{
        val regionSize: Int = when (region) {
            Constants.REGION_ALL -> {
                data.listStatesFromNet.size
            }
            else -> {
                data.listStatesFromNet.filter {
                    it.regionRus == data.region
                }.size
            }
        }
        return  "$region $regionSize"
    }

    fun updateImageStub() {
        dataFlags =  settingProvider.updateImageStub(dataFlags)
    }

    companion object{
        const val TAG = "33333"
    }

    //по типу ответа при щелчке по кнопке задаём состояние
    fun answer(guess:String){
        dataFlags = storage.getTypeAnswer(guess, dataFlags)
        when(dataFlags.typeAnswer){
            Answer.NotWell -> {
                currentQuizState.value = currentState.executeAction(Action.OnNotWellClicked(dataFlags))
            }
            Answer.WellNotLast -> {
                currentQuizState.value =  currentState.executeAction(Action.OnWellNotLastClicked(dataFlags))
            }
            Answer.WellAndLast -> {
                currentQuizState.value = currentState.executeAction(Action.OnWellAndLastClicked(dataFlags))
            }
            else -> {}
        }
    }

}