package com.bartex.statesmvvm.view.settings;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.bartex.statesmvvm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrefFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // создаём настройки из xml файла
      addPreferencesFromResource(R.xml.pref_setting);
    }
}