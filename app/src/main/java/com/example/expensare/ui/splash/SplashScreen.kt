package com.example.expensare.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import com.example.expensare.R
import com.example.expensare.ui.MainActivity
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash_screen)
      Log.d("SplashLOG", "I am started")
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
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
                    if (FirebaseAuth.getInstance().uid != null) {
                        val intent = Intent(this, MainActivity::class.java).apply {
                            putExtra("logged_in", true)
                        }
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, MainActivity::class.java).apply {
                            putExtra("logged_in", false)
                        }
                        startActivity(intent)
                    }
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
