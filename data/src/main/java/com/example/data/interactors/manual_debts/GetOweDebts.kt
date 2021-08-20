package com.example.data.interactors.manual_debts

import androidx.lifecycle.LiveData
import com.example.data.repositories.ManualDebtRepository
import com.example.domain.database.entities.ManualDebtEntity

class GetOweDebts(private val manualDebtRepository: ManualDebtRepository) {
    suspend operator fun invoke(): LiveData<ManualDebtEntity> =
        manualDebtRepository.getOweDebts()
}