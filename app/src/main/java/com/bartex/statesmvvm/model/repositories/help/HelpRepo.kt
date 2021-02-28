package com.bartex.statesmvvm.model.repositories.help

import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class HelpRepo(val app: App): IHelpRepo {

    override fun getHelpText(): String? {
        val iFile: InputStream = app.resources.openRawResource(R.raw.help_states)
        return inputStreamToString(iFile)
    }

    private fun inputStreamToString(iFile: InputStream): String? {
        val strFull = StringBuilder()
        var str: String? = ""
        try {
            // открываем поток для чтения
            val ir = InputStreamReader(iFile)
            val br = BufferedReader(ir)
            // читаем содержимое
            // в Java это  while ((str = br.readLine()) != null) {...}
            while (br.readLine().also { str = it } != null) {
                //Чтобы не было в одну строку, ставим символ новой строки
                strFull.append(str).append("\n")
            }
            //закрываем потоки
            iFile.close()
            ir.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return strFull.toString()
    }
}