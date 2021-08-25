package com.example.data.interactors.expenses

import com.example.data.repositories.ExpenseRepository
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.models.Expense

class GetAllExpenses(private val expenseRepository: ExpenseRepository) {

  suspend operator fun invoke(): ArrayList<Expense> = expenseRepository.getAll()
}
