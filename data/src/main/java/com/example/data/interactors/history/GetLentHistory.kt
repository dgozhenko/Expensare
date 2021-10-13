package com.example.data.interactors.history

import androidx.lifecycle.LiveData
import com.example.data.repositories.DebtHistoryRepository
import com.example.data.repositories.ManualDebtRepository
import com.example.domain.models.Debt
import com.example.domain.models.util.Response

class GetLentHistory(private val debtHistoryRepository: DebtHistoryRepository) {
    suspend operator fun invoke(): LiveData<Response<ArrayList<Debt>>> =
        debtHistoryRepository.getLentHistory()
}