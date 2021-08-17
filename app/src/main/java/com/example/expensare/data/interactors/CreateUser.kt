package com.example.expensare.data.interactors

import com.example.expensare.data.database.entities.UserEntity
import com.example.expensare.data.repositories.UserRepository

class CreateUser(private val userRepository: UserRepository) {

    suspend operator fun invoke(userEntity: UserEntity) = userRepository.createUser(userEntity)

}