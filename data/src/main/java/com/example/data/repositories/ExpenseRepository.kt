package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.ExpensesInterface
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.models.Expense
import com.example.domain.models.util.Response

class ExpenseRepository(private val expensesInterface: ExpensesInterface) {

  suspend fun create(expenseEntity: Expense): LiveData<Response<String>> {
    return expensesInterface.create(expenseEntity)
  }

  suspend fun getAll(): ArrayList<Expense> {
    return expensesInterface.getAll()
  }

  suspend fun download(): LiveData<Response<ArrayList<Expense>>> {
    return expensesInterface.download()
  }
}
