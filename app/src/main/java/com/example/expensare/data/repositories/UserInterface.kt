package com.example.expensare.data.repositories

import com.example.expensare.data.database.entities.UserEntity

interface UserInterface {

    suspend fun create(user: UserEntity)

    suspend fun getAll(): ArrayList<UserEntity>

    suspend fun downloadUser(): UserEntity

}