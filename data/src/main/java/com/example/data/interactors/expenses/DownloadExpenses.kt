package com.example.data.interactors.expenses

import androidx.lifecycle.LiveData
import com.example.data.repositories.ExpenseRepository
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.models.Expense
import com.example.domain.models.util.Response

class DownloadExpenses(private val expenseRepository: ExpenseRepository) {

  suspend operator fun invoke(): LiveData<Response<ArrayList<Expense>>> =
    expenseRepository.download()
}
