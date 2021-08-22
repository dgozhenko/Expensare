package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.database.entities.ManualDebtEntity
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Debt
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import com.example.domain.models.User
import java.util.ArrayList

interface ManualDebtInterface {
    suspend fun getUserInfo(): LiveData<User>
    suspend fun createDebt(debtFor: String, amount: Int, fromUser: User, toUser: User): SingleLiveEvent<Response<String>>
    suspend fun getLentDebts(): LiveData<Response<ArrayList<Debt>>>
    suspend fun getOweDebts(): LiveData<Response<ArrayList<Debt>>>
    suspend fun refreshLentDebts(): LiveData<Response<ArrayList<Debt>>>
    suspend fun refreshOweDebts(): LiveData<Response<ArrayList<Debt>>>
}