package com.example.expensare.ui.auth.avatar

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expensare.data.Avatar
import com.example.expensare.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception
import java.util.*

sealed class ChooseNameResult {
    object Success: ChooseNameResult()
    data class Error (val exception: Exception): ChooseNameResult()
}

class ChooseNameViewModel: ViewModel() {

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