package com.example.expensare.di.modules

import com.example.expensare.data.database.ExpensareDatabase
import com.example.expensare.data.repositories.*
import com.example.expensare.ui.storage.Storage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesUserDebtRepository(database: ExpensareDatabase): UserDebtRepository {
        return UserDebtRepository(database)
    }

    @Singleton
    @Provides
    fun providesRequestRepository(database: ExpensareDatabase): RequestRepository {
        return RequestRepository(database)
    }

    @Singleton
    @Provides
    fun providesListItemRepository(database: ExpensareDatabase): ListItemRepository {
        return ListItemRepository(database)
    }
    @Singleton
    @Provides
    fun providesExpenseRepository(database: ExpensareDatabase): ExpenseRepository {
        return ExpenseRepository(database)
    }

    @Singleton
    @Provides
    fun providesManualDebtRepository(database: ExpensareDatabase): ManualDebtRepository {
        return ManualDebtRepository(database)
    }

    @Singleton
    @Provides
    fun providesGroupRepository(database: ExpensareDatabase): GroupRepository {
        return GroupRepository(database)
    }

    @Singleton
    @Provides
    fun providesUserRepository(database: ExpensareDatabase): UserRepository {
        return UserRepository(database)
    }

}