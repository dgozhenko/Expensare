package com.example.data.interactors.manual_debts

import com.example.data.repositories.ManualDebtRepository
import com.example.domain.models.Debt
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent

class DeleteDebt (private val manualDebtRepository: ManualDebtRepository) {
    suspend operator fun invoke(debt: Debt): SingleLiveEvent<Response<String>> =
        manualDebtRepository.deleteDebt(debt)
}