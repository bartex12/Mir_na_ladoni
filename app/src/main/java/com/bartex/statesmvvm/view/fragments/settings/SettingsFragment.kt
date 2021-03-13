package com.bartex.statesmvvm.view.fragments.settings

import android.os.Bundle
import android.util.Log
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.bartex.statesmvvm.R

class SettingsFragment : PreferenceFragmentCompat() {

    companion object{
        const val TAG = "33333"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_setting, rootKey)

        findPreference<ListPreference>("ListColor")
            ?.setOnPreferenceChangeListener { preference, newValue ->
                val  oldTheme = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                    .getString("ListColor", "1")
                if (newValue != oldTheme){
                    Log.d(
                        TAG, "1 SettingsFragment ListPreferenceTheme newValue = $newValue" +
                            " oldTheme = $oldTheme ")
                    requireActivity().finish()
                }else{
                    Log.d(
                        TAG, "2 SettingsFragment ListPreferenceTheme newValue = $newValue" +
                            " oldTheme = $oldTheme ")
                }
                true
            }

        findPreference<ListPreference>("ListSort")
            ?.setOnPreferenceChangeListener { preference, newValue ->
                val  oldSort = PreferenceManager.getDefaultSharedPreferences(requireActivity())
                    .getString("ListSort", "3")
                if (newValue != oldSort){
                    Log.d(
                        TAG, "1 SettingsFragment ListPreferenceSort newValue = $newValue" +
                            " oldSort = $oldSort ")
                    requireActivity().finish()
                }else{
                    Log.d(
                        TAG, "2 SettingsFragment ListPreferenceSort newValue = $newValue" +
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