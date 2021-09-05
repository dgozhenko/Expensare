package com.example.data.interactors.manual_debts

import com.example.data.repositories.ManualDebtRepository
import com.example.domain.models.Debt
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent

class CreateRequest(private val manualDebtRepository: ManualDebtRepository) {
    suspend operator fun invoke(debt: Debt): SingleLiveEvent<Response<String>> =
        manualDebtRepository.createRequest(debt)
}