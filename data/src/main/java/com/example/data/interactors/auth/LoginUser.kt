package com.example.data.interactors.auth

import com.example.data.repositories.AuthRepository
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent

class LoginUser(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): SingleLiveEvent<Response<String>> =
        authRepository.login(email, password)
}
