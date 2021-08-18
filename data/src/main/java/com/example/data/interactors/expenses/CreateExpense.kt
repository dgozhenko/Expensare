package com.example.data.interactors.expenses

import com.example.data.repositories.ExpenseRepository
import com.example.domain.database.entities.ExpenseEntity

class CreateExpense(private val expenseRepository: ExpenseRepository) {

    suspend operator fun invoke(expenseEntity: ExpenseEntity) {
        return expenseRepository.create(expenseEntity)
    }
}