package com.bartex.statesmvvm.view.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.bartex.statesmvvm.R

//приходится через SettingsActivityЮ так как у Moxy нет  поддержки для PreferenceFragmentCompat
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_setting, rootKey)
    }
}