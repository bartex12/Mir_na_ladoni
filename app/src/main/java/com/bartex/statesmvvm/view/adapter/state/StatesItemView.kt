package com.bartex.statesmvvm.view.adapter.state

import com.bartex.statesmvvm.view.adapter.IItemView

interface StatesItemView: IItemView {
    fun setName(name: String)
    fun loadFlag(url: String)
}