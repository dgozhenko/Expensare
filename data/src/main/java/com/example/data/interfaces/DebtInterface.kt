package com.example.data.interfaces

import com.example.domain.models.UserDebt

interface DebtInterface {

  suspend fun create(debt: UserDebt)
}
