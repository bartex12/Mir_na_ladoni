package com.bartex.statesmvvm.view.fragments.home

import android.app.Application
import com.bartex.statesmvvm.R

class MainListImpl(private val application: Application):IMainList {

    override fun getList(): MutableList<ItemList> {
        return mutableListOf(
            ItemList(application.resources.getString(R.string.statesOfWorld), R.drawable.globus),
            ItemList(application.resources.getString(R.string.imageQuiz),R.drawable.single_pazzl),
            ItemList(application.resources.getString(R.string.settings),null),
            ItemList(application.resources.getString(R.string.help), null)
        )
    }
}