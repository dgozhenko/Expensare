package com.example.data.datasource

import androidx.lifecycle.LiveData
import com.example.data.interfaces.ManualDebtInterface
import com.example.domain.database.entities.ManualDebtEntity
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import com.example.domain.models.User
import javax.inject.Inject

class ManualDebtDataSource @Inject constructor() : ManualDebtInterface {
    override suspend fun getUserInfo(): LiveData<UserEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun createDebt(
        debtFor: String,
        amount: Int,
        fromUser: User,
        toUser: User
    ): SingleLiveEvent<Response<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLentDebts(): LiveData<ManualDebtEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getOweDebts(): LiveData<ManualDebtEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshLentDebts(): LiveData<ManualDebtEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshOweDebts(): LiveData<ManualDebtEntity> {
        TODO("Not yet implemented")
    }
}