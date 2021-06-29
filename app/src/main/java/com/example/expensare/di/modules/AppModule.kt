package com.example.expensare.di.modules

import android.content.Context
import com.example.expensare.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Singleton
    @Provides
    fun providesContext(): Context = app.baseContext

}