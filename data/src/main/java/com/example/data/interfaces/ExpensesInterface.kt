package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.models.Response

interface ExpensesInterface {

    suspend fun create(expenseEntity: ExpenseEntity): LiveData<Response<String>>

    suspend fun getAll(): ArrayList<ExpenseEntity>

    suspend fun download(): LiveData<Response<ArrayList<ExpenseEntity>>>
}