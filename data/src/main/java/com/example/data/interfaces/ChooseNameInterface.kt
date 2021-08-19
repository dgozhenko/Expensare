package com.example.data.interfaces

import android.net.Uri
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent

interface ChooseNameInterface {

    suspend fun createUserInDatabase(username: String, avatarUri: String, email: String): SingleLiveEvent<Response<String>>

    suspend fun uploadImage(uri: Uri, username: String, email: String): SingleLiveEvent<Response<Uri>>
}