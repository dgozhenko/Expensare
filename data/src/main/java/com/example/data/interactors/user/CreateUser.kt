package com.example.data.interactors.user

import com.example.data.repositories.UserRepository
import com.example.domain.database.entities.UserEntity

class CreateUser(private val userRepository: UserRepository) {

  suspend operator fun invoke(userEntity: UserEntity) = userRepository.createUser(userEntity)
}
