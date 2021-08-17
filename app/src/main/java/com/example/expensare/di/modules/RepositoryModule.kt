package com.example.expensare.di.modules

import com.example.data.datasource.ExpenseDataSource
import com.example.domain.database.ExpensareDatabase
import com.example.data.datasource.UserDataSource
import com.example.data.interactors.expenses.CreateExpense
import com.example.data.interactors.expenses.DownloadExpenses
import com.example.data.interactors.user.CreateUser
import com.example.data.interactors.user.DownloadUser
import com.example.data.interfaces.ExpensesInterface
import com.example.data.interfaces.UserInterface
import com.example.data.repositories.*
import com.example.data.storage.Storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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

    // Expenses

    @Singleton
    @Provides
    fun providesExpensesInterface(database: ExpensareDatabase, storage: Storage): ExpensesInterface {
        return ExpenseDataSource(database, storage)
    }

    @Singleton
    @Provides
    fun providesExpenseRepository(expensesInterface: ExpensesInterface): ExpenseRepository {
        return ExpenseRepository(expensesInterface)
    }

    @Singleton
    @Provides
    fun providesExpenseDataSource(database: ExpensareDatabase, storage: Storage): ExpenseDataSource {
        return ExpenseDataSource(database, storage)
    }

    @Singleton
    @Provides
    fun providesCreateExpense(expenseRepository: ExpenseRepository): CreateExpense {
        return CreateExpense(expenseRepository)
    }

    @Singleton
    @Provides
    fun providesDownloadExpense(expenseRepository: ExpenseRepository): DownloadExpenses {
        return DownloadExpenses(expenseRepository)
    }



    // User

    @Singleton
    @Provides
    fun providesUserInterface(database: ExpensareDatabase): UserInterface {
        return UserDataSource(database)
    }

    @Singleton
    @Provides
    fun providesUserDataSource(database: ExpensareDatabase): UserDataSource {
        return UserDataSource(database)
    }

    @Singleton
    @Provides
    fun providesUserRepository(userInterface: UserInterface): UserRepository {
        return UserRepository(userInterface)
    }

    @Singleton
    @Provides
    fun providesCreateUser(userRepository: UserRepository): CreateUser {
        return CreateUser(userRepository)
    }

    @Singleton
    @Provides
    fun providesDownloadUser(userRepository: UserRepository): DownloadUser {
        return DownloadUser(userRepository)
    }


}