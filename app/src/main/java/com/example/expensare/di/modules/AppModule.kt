package com.example.expensare.di.modules

import android.content.Context
import com.example.expensare.App
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule() {

  @Singleton
  @Provides
  fun provideApplication(@ApplicationContext application: Context): App {
    return application as App
  }
}
