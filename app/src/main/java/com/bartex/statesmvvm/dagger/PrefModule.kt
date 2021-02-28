package com.bartex.statesmvvm.dagger

import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.prefs.PreferenceHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PrefModule {

    @Provides
    @Singleton
    fun helperRepo(app: App): IPreferenceHelper =
        PreferenceHelper(app)
}