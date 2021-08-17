package com.example.data.repositories

import com.example.data.interfaces.UserInterface
import com.example.domain.database.entities.UserEntity

class UserRepository (private val userInterface: UserInterface) {

    suspend fun createUser(userEntity: UserEntity) {
        userInterface.create(userEntity)
    }

    suspend fun downloadUser() = userInterface.downloadUser()

    suspend fun getAll() = userInterface.getAll()
}