package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Response

interface UserInterface {

  suspend fun create(user: UserEntity)

  suspend fun getAll(): ArrayList<UserEntity>

  suspend fun downloadUser(): LiveData<Response<UserEntity>>
}
