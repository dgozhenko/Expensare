package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.UserInterface
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User
import com.example.domain.models.util.Response

class UserRepository(private val userInterface: UserInterface) {

  suspend fun createUser(user: User) {
    userInterface.create(user)
  }

  suspend fun downloadUser(): LiveData<Response<User>> = userInterface.downloadUser()

  suspend fun getAll() = userInterface.getAll()
}
