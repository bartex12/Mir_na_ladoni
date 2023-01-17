package com.bartex.statesmvvm.view.fragments.home

import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R

class MainListImpl(private val app: App = App.instance):IMainList {

    override fun getList(): MutableList<ItemList> {
        return mutableListOf(
            ItemList(app.resources.getString(R.string.statesOfWorld), R.drawable.globus),
            ItemList(app.resources.getString(R.string.imageQuiz),R.drawable.single_pazzl),
            ItemList(app.resources.getString(R.string.settings),null),
            ItemList(app.resources.getString(R.string.help), null)
        )
    }
}