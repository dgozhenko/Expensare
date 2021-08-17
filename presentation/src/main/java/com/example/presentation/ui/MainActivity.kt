package com.example.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.inner_circles_apps.myapplication.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAnalytics = Firebase.analytics
        val intentMessage = intent.getBooleanExtra("logged_in", false)
        if (intentMessage) {
            val navController = Navigation.findNavController(this, R.id.fragment)
            navController.navigate(R.id.dashboardFragment)
        } else {
            val navController = Navigation.findNavController(this, R.id.fragment)
            navController.navigate(R.id.loginFragment)
        }
    }
}