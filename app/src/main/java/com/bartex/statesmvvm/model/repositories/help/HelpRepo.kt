package com.bartex.statesmvvm.model.repositories.help

import android.app.Application
import android.content.Context
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R

class HelpRepo(val app: Application): IHelpRepo {

    override fun getHelpText(): String {
        return app.applicationContext.getString(R.string.helpString222)
    }
}