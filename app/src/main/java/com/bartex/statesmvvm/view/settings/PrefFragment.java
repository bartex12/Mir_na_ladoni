package com.bartex.statesmvvm.view.settings;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.bartex.statesmvvm.R;

public class PrefFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // создаём настройки из xml файла
      addPreferencesFromResource(R.xml.pref_setting);
    }



}