package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.models.Expense
import com.example.domain.models.util.Response

interface ExpensesInterface {

  suspend fun create(expenseEntity: Expense): LiveData<Response<String>>

  suspend fun getAll(): ArrayList<Expense>

  suspend fun download(): LiveData<Response<ArrayList<Expense>>>
}
