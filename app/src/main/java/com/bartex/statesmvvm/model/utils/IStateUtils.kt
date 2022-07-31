package com.bartex.statesmvvm.model.utils

import com.bartex.statesmvvm.model.entity.state.State

interface IStateUtils {
    fun getStateArea(area: Float?):String
    fun getStatePopulation(population: Int?):String
    fun getStatezoom(state: State?):String
    fun getStateCapital(capital: String?):String
    fun getStateRegion(region: String?):String
    fun getStateDensity(area: Float?, population: Int?):String
}