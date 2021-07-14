package com.example.expensare

import android.app.Application
import com.example.expensare.data.database.ExpensareDatabase
import com.example.expensare.di.ApplicationComponent
import com.example.expensare.di.DaggerApplicationComponent
import com.example.expensare.di.modules.AppModule
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso

class App : Application() {

    val appComponent: ApplicationComponent =
        DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()

    override fun onCreate() {
        super.onCreate()
        val built = Picasso.Builder(this).downloader(OkHttpDownloader(this, Long.MAX_VALUE)).build()
        built.setIndicatorsEnabled(false)
        built.isLoggingEnabled = true
        Picasso.setSingletonInstance(built)
    }
}