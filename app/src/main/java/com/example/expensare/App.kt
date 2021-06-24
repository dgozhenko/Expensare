package com.example.expensare

import android.app.Application
import com.example.expensare.di.ApplicationComponent
import com.example.expensare.di.DaggerApplicationComponent
import com.example.expensare.di.modules.AppModule

class App : Application() {

    val appComponent: ApplicationComponent =
        DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()
}