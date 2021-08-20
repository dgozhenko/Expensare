package com.example.data.interactors.manual_debts

import com.example.data.repositories.ManualDebtRepository
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import com.example.domain.models.User

class CreateDebt(private val manualDebtRepository: ManualDebtRepository) {
    suspend operator fun invoke(debtFor: String, amount: Int, fromUser: User, toUser: User): SingleLiveEvent<Response<String>> =
        manualDebtRepository.createDebt(debtFor, amount, fromUser, toUser)
}