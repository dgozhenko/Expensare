package com.example.data.interactors.expenses

import com.example.data.repositories.ExpenseRepository
import com.example.domain.database.entities.ExpenseEntity

class GetAllExpenses(private val expenseRepository: ExpenseRepository) {

    suspend operator fun invoke(): ArrayList<ExpenseEntity> = expenseRepository.getAll()
}