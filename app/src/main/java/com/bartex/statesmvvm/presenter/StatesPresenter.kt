package com.bartex.statesmvvm.presenter

import com.bartex.statesmvvm.Screens
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.presenter.base.BasePresenter
import io.reactivex.rxjava3.core.Single

//презентер для работы с фрагментом StatesFragment
class StatesPresenter: BasePresenter() {

    companion object{
        const val TAG = "33333"
    }

    override fun getListData(): Single<List<State>> = statesRepo.getStates()

    override fun navigateToScreen(state:State) {
        router.navigateTo(Screens.DetailsScreen(state))
    }

    fun getPosition(): Int{
        return helper.getPositionState()
    }

    fun savePosition(position: Int){
        helper.savePositionState(position)
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}