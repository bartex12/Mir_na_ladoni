package com.bartex.statesmvvm.view.fragments.quiz.setting

import android.content.Context
import android.media.AudioManager
import androidx.preference.PreferenceManager
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.view.fragments.quiz.fsm.entity.DataFlags

class SettingsProvider(val app: App = App.instance) : ISettingsProvider {

    override fun updateSoundOnOff() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)
        val sound = sharedPreferences.getBoolean(Constants.SOUND, true)

        (app.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
                .setStreamMute(AudioManager.STREAM_MUSIC, !sound)
    }

    override fun updateNumberFlagsInQuiz(dataFlags: DataFlags): DataFlags {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)
        val flagsNumber: String?  = sharedPreferences.getString(Constants.FLAGS_IN_QUIZ, 10.toString())
        flagsNumber?. let{
            dataFlags.flagsInQuiz =flagsNumber.toInt()
        }
        return dataFlags
    }

    override fun getGuessRows(dataFlags:DataFlags):DataFlags {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)
        // Получение количества отображаемых вариантов ответа
        val choices: String? = sharedPreferences.getString(Constants.CHOICES, 2.toString())
        choices?. let{
            dataFlags.guessRows = it.toInt() / 2
        }
        return dataFlags
    }

    override fun updateImageStub(dataFlags:DataFlags):DataFlags {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)
        val imageStub: String? = sharedPreferences.getString(Constants.IMAGE_STUB, 1.toString())
        imageStub?. let{
            dataFlags.incorrectImageStub =it.toInt()
        }
        return dataFlags
    }
}