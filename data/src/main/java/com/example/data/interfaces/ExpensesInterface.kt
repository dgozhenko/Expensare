package com.example.data.interfaces

import com.example.domain.database.entities.ExpenseEntity

interface ExpensesInterface {

    suspend fun create(expenseEntity: ExpenseEntity)

    suspend fun getAll(): ArrayList<ExpenseEntity>

    suspend fun download(): ArrayList<ExpenseEntity>
}