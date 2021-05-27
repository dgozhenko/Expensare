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
import java.util.*

class ChooseNameViewModel: ViewModel() {

    private val _isComplete = MutableLiveData<Boolean>()
    val isComplete: LiveData<Boolean> get() = _isComplete

        private fun createUserInDatabase(username: String, avatarUri: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val user = User(uid, username, avatarUri)
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/$uid")
        reference.setValue(user).addOnSuccessListener {
            Log.d("Registration", "User created in database")
            _isComplete.postValue(true)
        }
            .addOnFailureListener {
                Log.d("Registration", it.message!!)
            }
    }

    fun uploadImage(uri: Uri, username: String) {
        val filename = UUID.randomUUID().toString()
        val reference = FirebaseStorage.getInstance("gs://expensare.appspot.com").getReference("/avatars/$filename")
        reference.putFile(uri)
            .addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener {
                    createUserInDatabase(username, it.toString())
                }
            }
    }

}