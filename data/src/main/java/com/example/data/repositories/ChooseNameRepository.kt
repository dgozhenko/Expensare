package com.example.data.repositories

import android.net.Uri
import com.example.data.interfaces.ChooseNameInterface
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent

class ChooseNameRepository(private val chooseNameRepository: ChooseNameInterface) {

    suspend fun createUserInDatabase(username: String, avatarUri: String, email: String): SingleLiveEvent<Response<String>> =
        chooseNameRepository.createUserInDatabase(username, avatarUri, email)

    suspend fun uploadImage(uri: Uri, username: String, email: String): SingleLiveEvent<Response<Uri>> =
        chooseNameRepository.uploadImage(uri, username, email)
}