package com.example.data.repositories

import com.example.data.interfaces.DebtInterface
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import com.example.domain.models.GroupDebt

class DebtRepository(private val debtInterface: DebtInterface) {

  suspend fun create(debt: GroupDebt): SingleLiveEvent<Response<String>> = debtInterface.create(debt)
}
