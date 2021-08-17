package com.example.expensare.data.repositories

import com.example.expensare.data.database.ExpensareDatabase
import com.example.expensare.data.database.daos.UserDao
import com.example.expensare.data.database.entities.UserEntity
import javax.inject.Inject

class UserRepository (private val userInterface: UserInterface) {

    suspend fun createUser(userEntity: UserEntity) {
        userInterface.create(userEntity)
    }

    suspend fun downloadUser() = userInterface.downloadUser()

    suspend fun getAll() = userInterface.getAll()
}