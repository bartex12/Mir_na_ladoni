package com.bartex.statesmvvm.view.settings

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.bartex.statesmvvm.R
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

    companion object{
        const val TAG = "33333"
    }
    private var oldTheme:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "SettingsActivity onCreate ")
        //читаем сохранённную в настройках тему
        oldTheme = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("ListColor", "1")!!.toInt()
        //устанавливаем сохранённную в настройках тему
        when(oldTheme){
            1->setTheme(R.style.AppTheme)
            2->setTheme(R.style.AppThemeGreen)
            3->setTheme(R.style.AppThemePurple)
            4->setTheme(R.style.AppThemeRed)
            5->setTheme(R.style.AppThemeBlue)
            6->setTheme(R.style.AppThemeBlack)
        }
        setContentView(R.layout.settings_activity)

        //поддержка экшенбара
        setSupportActionBar(toolbar_settings)
        // отображаем фрагмент с настройками
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        //отображаем стрелку Назад в тулбаре
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //ставим слушатель нажатия на стрелку Назад в тулбаре
        toolbar_settings.setNavigationOnClickListener {
            onBackPressed()
        }
    }

   class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.pref_setting, rootKey)

            findPreference<ListPreference>("ListColor")
                ?.setOnPreferenceChangeListener { preference, newValue ->
                   val  oldTheme = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                        .getString("ListColor", "1")
                    if (newValue != oldTheme){
                        Log.d(TAG, "1 SettingsFragment ListPreferenceTheme newValue = $newValue" +
                                " oldTheme = $oldTheme ")
                        requireActivity().finish()
                    }else{
                        Log.d(TAG, "2 SettingsFragment ListPreferenceTheme newValue = $newValue" +
                                " oldTheme = $oldTheme ")
                    }
                true
            }

            findPreference<ListPreference>("ListSort")
                ?.setOnPreferenceChangeListener { preference, newValue ->
                    val  oldSort = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                        .getString("ListSort", "3")
                    if (newValue != oldSort){
                        Log.d(TAG, "1 SettingsFragment ListPreferenceSort newValue = $newValue" +
                                " oldSort = $oldSort ")
                        requireActivity().finish()
                    }else{
                        Log.d(TAG, "2 SettingsFragment ListPreferenceSort newValue = $newValue" +
                                " oldSort = $oldSort ")
                    }
                    true
                }

            findPreference<CheckBoxPreference>("cbSort")
                ?.setOnPreferenceChangeListener { preference, newValue ->
                    if (newValue == true){
                        Log.d(TAG, "1 SettingsFragment CheckBoxPreference newValue = $newValue")

                    }else{
                        Log.d(TAG, "2 SettingsFragment CheckBoxPreference newValue = $newValue")
                        requireActivity().finish()
                    }
                    true
                }
        }
    }
}