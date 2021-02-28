package com.bartex.statesmvvm.presenter

import android.util.Log
import com.bartex.statesmvvm.Screens
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import com.bartex.statesmvvm.view.main.MainView
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainPresenter: MvpPresenter<MainView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var  roomCash: IRoomStateCash

    companion object{
        const val TAG = "33333"
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.navigateTo(Screens.StatesScreen())
    }

    fun backClicked() {
        router.exit()
        router.exit() //выход с белого экрана App
    }

   fun  doSearch(search:String){
       Log.d(TAG, "MainPresenter doSearch search = $search ")
       router.navigateTo(Screens.SearchScreen(search))
   }

    fun  showHelp(){
        Log.d(TAG, "MainPresenter showHelp")
        router.navigateTo(Screens.HelpScreen())
    }

    fun showSettingsActivity(){
        router.navigateTo(Screens.SettingsScreen())
    }

    fun showFavorites(){
        router.navigateTo(Screens.FavoriteScreen())
    }

}