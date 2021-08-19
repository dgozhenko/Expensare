package com.example.data.interactors.auth

import com.example.data.repositories.AuthRepository
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent

class RegisterUser(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): SingleLiveEvent<Response<String>> =
        authRepository.register(email, password)
}