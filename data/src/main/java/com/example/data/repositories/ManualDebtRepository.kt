package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.ManualDebtInterface
import com.example.domain.database.entities.ManualDebtEntity
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Debt
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import com.example.domain.models.User
import java.util.ArrayList

class ManualDebtRepository(private val manualDebtInterface: ManualDebtInterface) {
    suspend fun getUserInfo(): LiveData<User> =
        manualDebtInterface.getUserInfo()

    suspend fun createDebt(debtFor: String, amount: Int, fromUser: User, toUser: User): SingleLiveEvent<Response<String>> =
        manualDebtInterface.createDebt(debtFor, amount, fromUser, toUser)

    suspend fun getLentDebts(): LiveData<Response<ArrayList<Debt>>> =
        manualDebtInterface.getLentDebts()

    suspend fun getOweDebts(): LiveData<Response<ArrayList<Debt>>> =
        manualDebtInterface.getOweDebts()

    suspend fun refreshLentDebts(): LiveData<Response<ArrayList<Debt>>> =
        manualDebtInterface.refreshLentDebts()

    suspend fun refreshOweDebts(): LiveData<Response<ArrayList<Debt>>> =
        manualDebtInterface.refreshOweDebts()

    suspend fun createRequest(debt: Debt): SingleLiveEvent<Response<String>> =
        manualDebtInterface.createRequest(debt)

    suspend fun deleteDebt(debt: Debt): SingleLiveEvent<Response<String>> =
        manualDebtInterface.deleteDebt(debt)
}