package com.bartex.statesmvvm.dagger

import com.bartex.statesmvvm.model.utils.IStateUtils
import com.bartex.statesmvvm.model.utils.StateUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilsModule {
    @Provides
    @Singleton
    fun utils(): IStateUtils =
        StateUtils()
}