package com.example.data.repositories

import com.example.data.interfaces.AuthInterface
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent

class AuthRepository(private val authInterface: AuthInterface) {

  suspend fun login(email: String, password: String): SingleLiveEvent<Response<String>> =
    authInterface.login(email, password)

  suspend fun register(email: String, password: String): SingleLiveEvent<Response<String>> =
    authInterface.register(email, password)
}
