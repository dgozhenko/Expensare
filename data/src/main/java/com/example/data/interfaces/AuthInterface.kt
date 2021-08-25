package com.example.data.interfaces

import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent

interface AuthInterface {

  suspend fun login(email: String, password: String): SingleLiveEvent<Response<String>>

  suspend fun register(email: String, password: String): SingleLiveEvent<Response<String>>
}
