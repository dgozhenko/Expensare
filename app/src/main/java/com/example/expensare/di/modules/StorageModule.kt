package com.example.expensare.di.modules

import android.content.Context
import com.example.expensare.ui.storage.Storage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun providesStorage(context: Context) = Storage(context)
}