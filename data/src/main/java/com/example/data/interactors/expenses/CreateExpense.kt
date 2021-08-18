package com.example.data.interactors.expenses

import androidx.lifecycle.LiveData
import com.example.data.repositories.ExpenseRepository
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.models.Response

class CreateExpense(private val expenseRepository: ExpenseRepository) {

    suspend operator fun invoke(expenseEntity: ExpenseEntity): LiveData<Response<String>> {
        return expenseRepository.create(expenseEntity)
    }
}