package com.example.data.interactors.debt

import com.example.data.repositories.DebtRepository
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import com.example.domain.models.GroupDebt

class CreateDebt(private val debtRepository: DebtRepository) {

  suspend operator fun invoke(debt: GroupDebt): SingleLiveEvent<Response<String>> = debtRepository.create(debt)
}
