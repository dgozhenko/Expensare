package com.example.expensare

import android.app.Application
import android.util.Log
import com.example.expensare.di.ApplicationComponent
import com.example.expensare.di.DaggerApplicationComponent
import com.example.expensare.di.modules.AppModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import kotlin.concurrent.timerTask

class App : Application() {

    val appComponent: ApplicationComponent =
        DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()

    override fun onCreate() {
        super.onCreate()
//        FirebaseDatabase.getInstance(
//            "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").setPersistenceEnabled(true)
        val built = Picasso.Builder(this).downloader(OkHttpDownloader(this, Long.MAX_VALUE)).build()
        built.setIndicatorsEnabled(false)
        built.isLoggingEnabled = true
        Picasso.setSingletonInstance(built)
    }
}