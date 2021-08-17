package com.example.data.interactors.expenses

import com.example.data.repositories.ExpenseRepository
import com.example.domain.database.entities.ExpenseEntity
import java.lang.Exception

class CreateExpense(private val expenseRepository: ExpenseRepository) {

    suspend operator fun invoke(expenseEntity: ExpenseEntity): Exception? {
        return expenseRepository.create(expenseEntity)
    }
}