package com.example.expensare.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.expensare.R
import com.example.expensare.ui.MainActivity
import com.google.android.material.progressindicator.LinearProgressIndicator

class SplashScreen : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash_screen)
    var i = 0
    val progressBar = findViewById<LinearProgressIndicator>(R.id.progress_bar)
    progressBar.trackColor = resources.getColor(R.color.light_black)
    progressBar.setIndicatorColor(resources.getColor(R.color.red))

    Thread {
        kotlin.run {
            while (i < 100) {
                i++
                Handler(Looper.getMainLooper()).post { kotlin.run { progressBar.progress = i } }
                if (i == 100) {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
                try {
                    Thread.sleep(8)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }.start()


  }
}
