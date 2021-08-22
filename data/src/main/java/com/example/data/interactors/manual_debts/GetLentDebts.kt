package com.example.data.interactors.manual_debts

import androidx.lifecycle.LiveData
import com.example.data.repositories.ManualDebtRepository
import com.example.domain.database.entities.ManualDebtEntity
import com.example.domain.models.Debt
import com.example.domain.models.util.Response

class GetLentDebts(private val manualDebtRepository: ManualDebtRepository) {
    suspend operator fun invoke(): LiveData<Response<ArrayList<Debt>>> =
        manualDebtRepository.getLentDebts()
}