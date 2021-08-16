package com.example.expensare.di.modules

import android.content.Context
import androidx.room.Room
import com.example.domain.database.ExpensareDatabase
import com.example.expensare.App
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(app: App): ExpensareDatabase {
        return Room.databaseBuilder(app, ExpensareDatabase::class.java, "expensare_db")
            .fallbackToDestructiveMigration()
            .build()
    }
}