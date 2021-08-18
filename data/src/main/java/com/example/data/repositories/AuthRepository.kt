package com.example.data.repositories

import com.example.data.interfaces.AuthInterface
import java.lang.Exception

class AuthRepository(private val authInterface: AuthInterface) {
    suspend fun login(email: String, password: String) = authInterface.login(email, password)
}