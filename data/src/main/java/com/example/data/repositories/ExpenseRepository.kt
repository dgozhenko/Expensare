package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.ExpensesInterface
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.models.Response

class ExpenseRepository(private val expensesInterface: ExpensesInterface) {

    suspend fun create(expenseEntity: ExpenseEntity): LiveData<Response<String>> {
        return expensesInterface.create(expenseEntity)
    }

    suspend fun getAll(): ArrayList<ExpenseEntity> {
        return expensesInterface.getAll()
    }

    suspend fun download(): LiveData<Response<ArrayList<ExpenseEntity>>> {
        return expensesInterface.download()
    }

}