package com.bartex.statesmvvm.model.repositories.prefs

interface IPreferenceHelper {
    fun savePositionState(position:Int)
    fun getPositionState(): Int

    fun savePositionSearch(position:Int)
    fun getPositionSearch(): Int

    fun savePositionFavorite(position:Int)
    fun getPositionFavorite(): Int

    fun getSortCase():Int
    fun isSorted():Boolean

   fun  saveTextSearch(text:String)

    fun saveTheme()

    fun getTheme():Boolean
}