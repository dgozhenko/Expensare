package com.example.data.interfaces

interface AuthInterface {
    suspend fun login(email: String, password: String)
}