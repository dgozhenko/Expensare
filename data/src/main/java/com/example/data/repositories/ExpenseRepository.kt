package com.example.data.repositories

import com.example.data.interfaces.ExpensesInterface
import com.example.domain.database.entities.ExpenseEntity

class ExpenseRepository(private val expensesInterface: ExpensesInterface) {

    suspend fun create(expenseEntity: ExpenseEntity) {
        return expensesInterface.create(expenseEntity)
    }

    suspend fun getAll(): ArrayList<ExpenseEntity> {
        return expensesInterface.getAll()
    }

    suspend fun download(): ArrayList<ExpenseEntity> {
        return expensesInterface.download()
    }

}