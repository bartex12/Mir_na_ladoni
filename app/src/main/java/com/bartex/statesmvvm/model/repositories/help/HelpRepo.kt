package com.bartex.statesmvvm.model.repositories.help

import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class     HelpRepo(var app: App): IHelpRepo {

    override fun getHelpText(): String? {
//        val iFile: InputStream = app.resources.openRawResource(R.raw.help_states)
//        return inputStreamToString(iFile)
        return app.applicationContext.getString(R.string.helpString222)
    }
}