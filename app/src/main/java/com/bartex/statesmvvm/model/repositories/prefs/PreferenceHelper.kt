package com.bartex.statesmvvm.model.repositories.prefs

import androidx.preference.PreferenceManager
import android.util.Log
import com.bartex.statesmvvm.App

class PreferenceHelper(val app: App):
    IPreferenceHelper {

    companion object{
        const val TAG = "33333"
        const val FIRST_POSITION = "FIRST_POSITION"
        const val FIRST_POSITION_SEARCH = "FIRST_POSITION_SEARCH"
        const val TEXT_SEARCH = "TEXT_SEARCH"
        const val FIRST_POSITION_FAVORITE = "FIRST_POSITION_FAVORITE"
        const val THEME = "THEME"
    }

    override fun savePositionState(position:Int) {

        PreferenceManager.getDefaultSharedPreferences(app)
            .edit()
            .putInt(FIRST_POSITION, position)
            .apply()
        Log.d(TAG,"PreferenceHelper savePosition position = $position"
        )
    }

    override fun getPositionState(): Int {
        val position = PreferenceManager.getDefaultSharedPreferences(app)
            .getInt(FIRST_POSITION, 0)
        Log.d(TAG, "PreferenceHelper getPosition FIRST_POSITION = $position")
        return position
    }

    override fun savePositionSearch(position: Int) {
        PreferenceManager.getDefaultSharedPreferences(app)
            .edit()
            .putInt(FIRST_POSITION_SEARCH, position)
            .apply()
        Log.d(TAG,"PreferenceHelper savePositionSearch position = $position"
        )
    }

    override fun getPositionSearch(): Int {
        val position = PreferenceManager.getDefaultSharedPreferences(app)
            .getInt(FIRST_POSITION_SEARCH, 0)
        Log.d(TAG, "PreferenceHelper getPositionSearch FIRST_POSITION_SEARCH = $position")
        return position
    }

    override fun savePositionFavorite(position: Int) {
        PreferenceManager.getDefaultSharedPreferences(app)
            .edit()
            .putInt(FIRST_POSITION_FAVORITE, position)
            .apply()
        Log.d(TAG,"PreferenceHelper savePositionFavorite position = $position"
        )
    }

    override fun getPositionFavorite(): Int {
        val position = PreferenceManager.getDefaultSharedPreferences(app)
            .getInt(FIRST_POSITION_FAVORITE, 0)
        Log.d(TAG, "PreferenceHelper getPositionFavorite FIRST_POSITION_FAVORITE = $position")
        return position
    }

    //получаем файл с настройками сортировки для приложения
    override fun getSortCase(): Int {
        val prefSetting = PreferenceManager.getDefaultSharedPreferences(app)
        return prefSetting.getString("ListSort", "3")!!.toInt()
    }

    //получаем файл с настройками для приложения - нужна ли сортировка
    override fun isSorted(): Boolean {
        val prefSetting = PreferenceManager.getDefaultSharedPreferences(app)
        return prefSetting.getBoolean("cbSort", true)
    }

    override fun saveTextSearch(text: String) {
        PreferenceManager.getDefaultSharedPreferences(app)
            .edit()
            .putString(TEXT_SEARCH, text)
            .apply()
        Log.d(TAG,"PreferenceHelper savePositionSearch TEXT_SEARCH = $text"
        )
    }

    override fun saveTheme() {
        PreferenceManager.getDefaultSharedPreferences(app)
            .edit()
            .putBoolean(THEME, ! getTheme())
            .apply()
        Log.d(TAG,"PreferenceHelper savePositionSearch THEME = true"
        )
    }

    override fun getTheme():Boolean {
        val prefSetting = PreferenceManager.getDefaultSharedPreferences(app)
        return prefSetting.getBoolean(THEME, false)
    }

}