package com.example.data.interactors.user

import com.example.data.repositories.UserRepository
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User

class CreateUser(private val userRepository: UserRepository) {

  suspend operator fun invoke(user: User) = userRepository.createUser(user)
}
