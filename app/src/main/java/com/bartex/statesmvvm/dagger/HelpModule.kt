package com.bartex.statesmvvm.dagger

import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.model.repositories.help.HelpRepo
import com.bartex.statesmvvm.model.repositories.help.IHelpRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HelpModule {

    @Provides
    @Singleton
    fun helpRepo(app: App): IHelpRepo =
        HelpRepo(app)
}