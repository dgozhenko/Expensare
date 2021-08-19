package com.example.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Status
import com.example.presentation.ui.MainActivity
import com.example.presentation.ui.dashboard.DashboardFragmentDirections
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.inner_circles_apps.myapplication.R
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private val splashScreenViewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash_screen)

    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    var i = 0
    val progressBar = findViewById<LinearProgressIndicator>(R.id.progress_bar)
    progressBar.trackColor = resources.getColor(R.color.light_black)
    progressBar.setIndicatorColor(resources.getColor(R.color.red))
        progressBar.isIndeterminate = true

        if (FirebaseAuth.getInstance().uid != null) {
            splashScreenViewModel.user.observe(this, {
                when(it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        val intent = Intent(this, MainActivity::class.java).apply {
                            putExtra("logged_in", "Dashboard")
                        }
                        startActivity(intent)
                        finish()
                    }
                    Status.ERROR -> {
                        if (it.data == UserEntity.EMPTY) {
                            progressBar.visibility = View.GONE
                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra("logged_in", "ChooseName")
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            })
        } else {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("logged_in", "Login")
            }
            startActivity(intent)
            finish()
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
  }
}
