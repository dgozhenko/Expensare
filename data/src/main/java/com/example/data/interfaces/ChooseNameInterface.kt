package com.example.data.interfaces

import android.net.Uri
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent

interface ChooseNameInterface {

    suspend fun createUserInDatabase(username: String, avatarUri: String, email: String): SingleLiveEvent<Response<String>>

    suspend fun uploadImage(uri: Uri, username: String, email: String): SingleLiveEvent<Response<Uri>>
}