package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.models.Debt
import com.example.domain.models.util.Response
import java.util.ArrayList

interface DebtHistoryInterface {
    suspend fun getLentHistory(): LiveData<Response<ArrayList<Debt>>>
    suspend fun getOweHistory(): LiveData<Response<ArrayList<Debt>>>
}