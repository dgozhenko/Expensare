package com.example.data.interactors.user

import com.example.domain.database.entities.UserEntity
import com.example.data.repositories.UserRepository


class CreateUser(private val userRepository: UserRepository) {

    suspend operator fun invoke(userEntity: UserEntity) = userRepository.createUser(userEntity)

}