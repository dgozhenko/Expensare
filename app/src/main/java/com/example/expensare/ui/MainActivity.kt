package com.example.expensare.ui

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Directory.ACCOUNT_TYPE
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.expensare.R
import com.example.expensare.ui.dashboard.DashboardFragment
import com.example.expensare.ui.splash.SplashScreen
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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