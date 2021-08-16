package com.example.data.interactors.user

import com.example.data.repositories.UserRepository

class GetAllUsers(private val userRepository: UserRepository) {

    suspend operator fun invoke() = userRepository.getAll()

}