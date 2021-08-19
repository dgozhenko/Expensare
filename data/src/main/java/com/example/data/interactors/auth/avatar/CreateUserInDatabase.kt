package com.example.data.interactors.auth.avatar

import com.example.data.repositories.ChooseNameRepository
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent

class CreateUserInDatabase(private val chooseNameRepository: ChooseNameRepository) {
    suspend operator fun invoke(
        username: String,
        avatarUri: String,
        email: String
    ): SingleLiveEvent<Response<String>> =
        chooseNameRepository.createUserInDatabase(username, avatarUri, email)
}