package com.example.data.interactors.user

import com.example.data.repositories.UserRepository

class DownloadUser(private val userRepository: UserRepository) {

    suspend operator fun invoke() = userRepository.downloadUser()
}