package com.example.data.interactors.user

import androidx.lifecycle.LiveData
import com.example.data.repositories.UserRepository
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Response

class DownloadUser(private val userRepository: UserRepository) {

  suspend operator fun invoke(): LiveData<Response<UserEntity>> = userRepository.downloadUser()
}
