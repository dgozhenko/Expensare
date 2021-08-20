package com.example.data.interfaces

import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import com.example.domain.models.GroupDebt

interface DebtInterface {

  suspend fun create(debt: GroupDebt) : SingleLiveEvent<Response<String>>
}
