package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.AuthInterface
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import java.lang.Exception

class AuthRepository(private val authInterface: AuthInterface) {
    suspend fun login(email: String, password: String): SingleLiveEvent<Response<String>> = authInterface.login(email, password)
}