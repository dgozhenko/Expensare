package com.example.data.interactors.auth.avatar

import android.net.Uri
import com.example.data.repositories.ChooseNameRepository
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent

class UploadImage(private val chooseNameRepository: ChooseNameRepository) {
    suspend operator fun invoke(uri: Uri, username: String, email: String): SingleLiveEvent<Response<Uri>> =
        chooseNameRepository.uploadImage(uri, username, email)
}