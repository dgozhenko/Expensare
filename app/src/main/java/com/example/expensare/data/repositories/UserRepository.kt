package com.example.expensare.data.repositories

import com.example.expensare.data.database.ExpensareDatabase
import com.example.expensare.data.database.daos.UserDao
import com.example.expensare.data.database.entities.UserEntity
import javax.inject.Inject

class UserRepository (database: ExpensareDatabase) {

    private val createUserDao: UserDao = database.userDao()

    suspend fun createUserInDatabase(userEntity: UserEntity) {
        createUserDao.createUser(userEntity)
    }

    suspend fun getUsers(): ArrayList<UserEntity> {
        return createUserDao.getAllUsers() as ArrayList<UserEntity>
    }

    fun createUserInFirebase(userEntity: UserEntity) {

    }

}