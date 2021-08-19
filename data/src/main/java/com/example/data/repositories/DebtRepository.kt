package com.example.data.repositories

import com.example.data.interfaces.DebtInterface
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import com.example.domain.models.UserDebt

class DebtRepository(private val debtInterface: DebtInterface) {

  suspend fun create(debt: UserDebt): SingleLiveEvent<Response<String>> = debtInterface.create(debt)
}
