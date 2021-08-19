package com.example.data.interfaces

import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import com.example.domain.models.UserDebt

interface DebtInterface {

  suspend fun create(debt: UserDebt) : SingleLiveEvent<Response<String>>
}
