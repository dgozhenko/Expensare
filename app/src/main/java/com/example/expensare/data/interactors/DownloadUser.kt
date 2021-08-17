package com.example.expensare.data.interactors

import com.example.expensare.data.repositories.UserInterface
import com.example.expensare.data.repositories.UserRepository

class DownloadUser(private val userRepository: UserRepository) {

    suspend operator fun invoke() = userRepository.downloadUser()
}