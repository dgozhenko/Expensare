package com.example.data.interactors.history

import androidx.lifecycle.LiveData
import com.example.data.repositories.DebtHistoryRepository
import com.example.domain.models.Debt
import com.example.domain.models.util.Response

class GetOweHistory(private val debtHistoryRepository: DebtHistoryRepository) {
    suspend operator fun invoke(): LiveData<Response<ArrayList<Debt>>> =
        debtHistoryRepository.getOweHistory()
}