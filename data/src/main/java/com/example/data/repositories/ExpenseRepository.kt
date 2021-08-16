package com.example.data.repositories

import com.example.data.interfaces.ExpensesInterface
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.daos.ExpenseDao
import com.example.domain.database.entities.ExpenseEntity

class ExpenseRepository(private val expensesInterface: ExpensesInterface) {

    suspend fun create(expenseEntity: ExpenseEntity) {
        expensesInterface.create(expenseEntity)
    }

    suspend fun getAll(): ArrayList<ExpenseEntity> {
        return expensesInterface.getAll()
    }

    suspend fun download(): ArrayList<ExpenseEntity> {
        return expensesInterface.download()
    }

}