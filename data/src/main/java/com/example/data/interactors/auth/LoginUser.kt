package com.example.data.interactors.auth

import com.example.data.repositories.AuthRepository

class LoginUser(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = authRepository.login(email, password)
}