package com.example.data.interfaces

import com.example.domain.database.entities.UserEntity

interface UserInterface {

    suspend fun create(user: UserEntity)

    suspend fun getAll(): ArrayList<UserEntity>

    suspend fun downloadUser(): UserEntity

}