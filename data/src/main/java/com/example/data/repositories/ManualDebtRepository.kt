package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.ManualDebtInterface
import com.example.domain.database.entities.ManualDebtEntity
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.ManualDebt
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import com.example.domain.models.User

class ManualDebtRepository(private val manualDebtInterface: ManualDebtInterface) {
    suspend fun getUserInfo(): LiveData<UserEntity> =
        manualDebtInterface.getUserInfo()

    suspend fun createDebt(debtFor: String, amount: Int, fromUser: User, toUser: User): SingleLiveEvent<Response<String>> =
        manualDebtInterface.createDebt(debtFor, amount, fromUser, toUser)

    suspend fun getLentDebts(): LiveData<ManualDebtEntity> =
        manualDebtInterface.getLentDebts()

    suspend fun getOweDebts(): LiveData<ManualDebtEntity> =
        manualDebtInterface.getOweDebts()

    suspend fun refreshLentDebts(): LiveData<ManualDebtEntity> =
        manualDebtInterface.refreshLentDebts()

    suspend fun refreshOweDebts(): LiveData<ManualDebtEntity> =
        manualDebtInterface.refreshOweDebts()
}