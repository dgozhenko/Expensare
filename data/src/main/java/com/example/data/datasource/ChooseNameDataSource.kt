package com.example.data.datasource

import android.net.Uri
import com.example.data.interfaces.ChooseNameInterface
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import javax.inject.Inject

class ChooseNameDataSource @Inject constructor(): ChooseNameInterface {

    override suspend fun createUserInDatabase(
        username: String,
        avatarUri: String,
        email: String
    ): SingleLiveEvent<Response<String>> {
        val response: SingleLiveEvent<Response<String>> = SingleLiveEvent()
        response.value = Response.loading(null)
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val user = User(uid = uid,username = username, email = email, avatar = avatarUri, groups = null, null)
        val users =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/users/$uid")
        users.setValue(user).addOnSuccessListener {
            response.value = Response.success("User is successfully created")
        }
            .addOnFailureListener {
                response.value = Response.error(it.message!!, null)
            }
            .addOnCanceledListener {
                response.value = Response.error("Something wrong", null)
            }
        return response
    }

    override suspend fun uploadImage(
        uri: Uri,
        username: String,
        email: String
    ): SingleLiveEvent<Response<Uri>> {
        val response: SingleLiveEvent<Response<Uri>> = SingleLiveEvent()
        response.value = Response.loading(null)
        val filename = UUID.randomUUID().toString()
        val reference = FirebaseStorage.getInstance("gs://expensare.appspot.com")
            .getReference("/avatars/$filename")
        reference.putFile(uri)
            .addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener {
                    response.value = Response.success(it)
                }
            }
        return response
    }
}