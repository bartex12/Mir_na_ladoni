package com.bartex.statesmvvm.view.settings

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(MainActivity.TAG, "SettingsActivity onCreate ")
        setContentView(R.layout.activity_setting)

        setSupportActionBar(toolbar_settings) //поддержка экшенбара для создания строки поиска
        supportActionBar?.title =getString(R.string.sett)
        //отображаем стрелку Назад в тулбаре
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //ставим слушатель нажатия на стрелку Назад в тулбаре
        toolbar_settings.setNavigationOnClickListener {
            onBackPressed()
        }

        // отображаем фрагмент с настройками
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_settings, SettingsFragment())
            .commit()
    }
}