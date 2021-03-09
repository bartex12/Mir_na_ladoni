package com.bartex.statesmvvm.model.utils

import com.bartex.statesmvvm.model.entity.state.State

interface IStateUtils {
    fun getStateArea(state: State?):String
    fun getStatePopulation(state: State?):String
    fun getStatezoom(state: State?):String
    fun getStateCapital(state: State?):String
    fun getStateRegion(state: State?):String

}