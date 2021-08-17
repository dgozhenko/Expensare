package com.example.expensare.data.repositories

import com.example.expensare.data.database.ExpensareDatabase
import com.example.expensare.data.database.daos.ExpenseDao
import com.example.expensare.data.database.entities.ExpenseEntity

class ExpenseRepository(database: ExpensareDatabase) {

    private val expenseDao: ExpenseDao = database.expenseDao()

    suspend fun createExpenseInDatabase(expenseEntity: ExpenseEntity) {
        expenseDao.createExpense(expenseEntity)
    }

    suspend fun getAllExpenses(): ArrayList<ExpenseEntity> {
        return expenseDao.getExpenses() as ArrayList<ExpenseEntity>
    }

   suspend fun downloadAllExpenses(expenses: ArrayList<ExpenseEntity>) {
        expenseDao.downloadExpenses(expenses)
    }

    fun createExpenseInFirebase(expenseEntity: ExpenseEntity) {

    }
}