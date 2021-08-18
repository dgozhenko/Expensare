package com.example.expensare

import android.app.Application
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    val built = Picasso.Builder(this).downloader(OkHttpDownloader(this, Long.MAX_VALUE)).build()
    built.setIndicatorsEnabled(false)
    built.isLoggingEnabled = true
    Picasso.setSingletonInstance(built)
  }
}
