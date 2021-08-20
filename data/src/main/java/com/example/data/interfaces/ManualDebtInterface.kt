package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.database.entities.ManualDebtEntity
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import com.example.domain.models.User

interface ManualDebtInterface {
    suspend fun getUserInfo(): LiveData<UserEntity>
    suspend fun createDebt(debtFor: String, amount: Int, fromUser: User, toUser: User): SingleLiveEvent<Response<String>>
    suspend fun getLentDebts(): LiveData<ManualDebtEntity>
    suspend fun getOweDebts(): LiveData<ManualDebtEntity>
    suspend fun refreshLentDebts(): LiveData<ManualDebtEntity>
    suspend fun refreshOweDebts(): LiveData<ManualDebtEntity>
}