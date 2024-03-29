package com.bartex.statesmvvm.model.repositories.prefs

import androidx.preference.PreferenceManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bartex.statesmvvm.App

class PreferenceHelper(val app: App):
    IPreferenceHelper {

    companion object{
        const val TAG = "33333"
        const val FIRST_POSITION_STATE = "FIRST_POSITION_STATE"
        const val FIRST_POSITION_SEARCH = "FIRST_POSITION_SEARCH"
        const val TEXT_SEARCH = "TEXT_SEARCH"
        const val FIRST_POSITION_FAVORITE = "FIRST_POSITION_FAVORITE"
        const val THEME = "THEME"
        const val LANG = "LANG"
    }

    override fun savePositionState(position:Int) {

        PreferenceManager.getDefaultSharedPreferences(app)
            .edit()
            .putInt(FIRST_POSITION_STATE, position)
            .apply()
        Log.d(TAG,"PreferenceHelper savePosition position = $position"
        )
    }

    override fun getPositionState(): Int {
        val position = PreferenceManager.getDefaultSharedPreferences(app)
            .getInt(FIRST_POSITION_STATE, 0)
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

    override fun getTheme():LiveData<Int> {
        val data = MutableLiveData<Int>()
        data.value = PreferenceManager.getDefaultSharedPreferences(app)
            .getString("ListColor", "1")!!.toInt()
        return data
    }

    override fun getRusLang():Boolean {
        val prefSetting = PreferenceManager.getDefaultSharedPreferences(app)
        return prefSetting.getBoolean("switchLang", true)
    }

}