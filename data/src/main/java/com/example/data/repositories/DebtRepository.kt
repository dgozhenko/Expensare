package com.example.data.repositories

import com.example.data.interfaces.DebtInterface
import com.example.domain.models.UserDebt

class DebtRepository(private val debtInterface: DebtInterface) {

    suspend fun create(debt: UserDebt) = debtInterface.create(debt)

}