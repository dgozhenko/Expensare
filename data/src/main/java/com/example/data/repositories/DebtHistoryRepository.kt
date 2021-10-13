package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.DebtHistoryInterface
import com.example.domain.models.Debt
import com.example.domain.models.util.Response
import java.util.ArrayList

class DebtHistoryRepository (private val debtHistoryInterface: DebtHistoryInterface){
    suspend fun getLentHistory(): LiveData<Response<ArrayList<Debt>>> =
        debtHistoryInterface.getLentHistory()

    suspend fun getOweHistory(): LiveData<Response<ArrayList<Debt>>> =
        debtHistoryInterface.getOweHistory()
}