package com.example.data.interactors.debt

import com.example.data.repositories.DebtRepository
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import com.example.domain.models.UserDebt

class CreateDebt(private val debtRepository: DebtRepository) {

  suspend operator fun invoke(debt: UserDebt): SingleLiveEvent<Response<String>> = debtRepository.create(debt)
}
