package com.example.expensare.di.modules

import android.content.Context
import androidx.room.Room
import com.example.expensare.App
import com.example.expensare.data.database.ExpensareDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Singleton
    @Provides
    fun providesContext(): Context = app.baseContext

    @Singleton
    @Provides
    fun providesRoomDatabase(context: Context): ExpensareDatabase {
        return Room.databaseBuilder(context, ExpensareDatabase::class.java, "expensare_db")
            .fallbackToDestructiveMigration()
            .build()
    }
}