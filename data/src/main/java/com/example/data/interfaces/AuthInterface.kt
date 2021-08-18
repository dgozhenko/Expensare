package com.example.data.interfaces

import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent

interface AuthInterface {
  suspend fun login(email: String, password: String): SingleLiveEvent<Response<String>>
}
