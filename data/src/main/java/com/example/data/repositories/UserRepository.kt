package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.UserInterface
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Response

class UserRepository (private val userInterface: UserInterface) {

    suspend fun createUser(userEntity: UserEntity) {
        userInterface.create(userEntity)
    }

    suspend fun downloadUser(): LiveData<Response<UserEntity>> = userInterface.downloadUser()

    suspend fun getAll() = userInterface.getAll()
}