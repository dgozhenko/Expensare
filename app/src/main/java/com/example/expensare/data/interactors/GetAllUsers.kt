package com.example.expensare.data.interactors

import com.example.expensare.data.repositories.UserRepository

class GetAllUsers(private val userRepository: UserRepository) {

    suspend operator fun invoke() = userRepository.getAll()

}