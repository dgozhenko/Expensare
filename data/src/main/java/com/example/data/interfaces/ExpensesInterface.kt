package com.example.data.interfaces

import com.example.domain.database.entities.ExpenseEntity
import java.lang.Exception

interface ExpensesInterface {

    suspend fun create(expenseEntity: ExpenseEntity): Exception?

    suspend fun getAll(): ArrayList<ExpenseEntity>

    suspend fun download(): ArrayList<ExpenseEntity>
}