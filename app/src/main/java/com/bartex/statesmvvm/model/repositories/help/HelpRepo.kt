package com.bartex.statesmvvm.model.repositories.help

import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R

class     HelpRepo(var app: App): IHelpRepo {

    override fun getHelpText(): String {
        return app.applicationContext.getString(R.string.helpString222)
    }
}