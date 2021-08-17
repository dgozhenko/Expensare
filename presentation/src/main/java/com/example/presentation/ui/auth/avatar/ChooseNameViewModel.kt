package com.example.presentation.ui.auth.avatar

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.util.*
import javax.inject.Inject

sealed class ChooseNameResult {
    object Success: ChooseNameResult()
    data class Error (val exception: Exception): ChooseNameResult()
}

@HiltViewModel
class ChooseNameViewModel @Inject constructor(): ViewModel() {

    private val _chooseNameResult = MutableLiveData<ChooseNameResult>()
    val chooseNameResult: LiveData<ChooseNameResult> get() = _chooseNameResult

        private fun createUserInDatabase(username: String, avatarUri: String, email: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val user = User(uid, username, email, avatarUri)
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/$uid")
        reference.setValue(user).addOnSuccessListener {
           _chooseNameResult.postValue(ChooseNameResult.Success)
        }
            .addOnFailureListener {
                _chooseNameResult.postValue(ChooseNameResult.Error(it))
            }
    }

    fun uploadImage(uri: Uri, username: String, email: String) {
        val filename = UUID.randomUUID().toString()
        val reference = FirebaseStorage.getInstance("gs://expensare.appspot.com").getReference("/avatars/$filename")
        reference.putFile(uri)
            .addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener {
                    createUserInDatabase(username, it.toString(), email)
                }
            }
    }

}