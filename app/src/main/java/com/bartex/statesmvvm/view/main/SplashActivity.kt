package com.bartex.statesmvvm.view.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import androidx.preference.PreferenceManager
import com.bartex.statesmvvm.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //читаем сохранённный в настройках тип картинки
        val  isShowScreen = PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean("cbScreen", true)
        if(isShowScreen){
            setContentView(R.layout.activity_splash)
            Handler(Looper.getMainLooper()).postDelayed({
                image_view_splash.animate()
                    .scaleY(2f)
                    .scaleX(2f)
                    .setInterpolator(LinearInterpolator()).setDuration(3000)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                    })
            }, 300)
        }else{
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}