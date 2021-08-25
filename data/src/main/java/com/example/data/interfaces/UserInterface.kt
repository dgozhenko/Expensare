package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User
import com.example.domain.models.util.Response

interface UserInterface {

  suspend fun create(user: User)

  suspend fun getAll(): ArrayList<User>

  suspend fun downloadUser(): LiveData<Response<User>>
}
