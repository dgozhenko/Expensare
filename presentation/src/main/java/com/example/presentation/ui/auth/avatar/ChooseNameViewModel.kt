package com.example.presentation.ui.auth.avatar

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.interactors.auth.avatar.CreateUserInDatabase
import com.example.data.interactors.auth.avatar.UploadImage
import com.example.data.storage.Storage
import com.example.domain.models.Response
import com.example.domain.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import javax.inject.Inject

sealed class ChooseNameResult {
    object Success : ChooseNameResult()
    data class Error(val exception: Exception) : ChooseNameResult()
}

@HiltViewModel
class ChooseNameViewModel @Inject constructor(private val storage: Storage,
private val uploadImage: UploadImage,
private val createUserInDatabase: CreateUserInDatabase) : ViewModel() {

    private val _chooseNameResult = MutableLiveData<Response<Uri>>()
    val chooseNameResult: LiveData<Response<Uri>> get() = _chooseNameResult

    private val _createUserInDatabase = MutableLiveData<Response<String>>()
    val createUser: LiveData<Response<String>> get() = _createUserInDatabase

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _avatar = MutableLiveData<String>()
    val avatar: LiveData<String> get() = _avatar

    init {
        getStoredAvatar()
        getStoredEmail()
    }

    private fun getStoredEmail() {
        _email.postValue(storage.userEmail)
    }

    private fun getStoredAvatar() {
        _avatar.postValue(storage.userAvatar)
    }

    fun deleteStoredAvatar() {
        storage.userAvatar = "def"
    }

    fun deleteStoredEmail() {
        storage.userEmail = "def"
    }

    // TODO: 17.08.2021 Repository
     fun createUserInDatabase(username: String, avatarUri: String, email: String) {
       viewModelScope.launch(Dispatchers.Main) {
           createUserInDatabase.invoke(username, avatarUri, email).observeForever {
               _createUserInDatabase.postValue(it)
           }
       }
    }

    // TODO: 17.08.2021 Repository
    fun uploadImage(uri: Uri, username: String, email: String) {
        viewModelScope.launch(Dispatchers.Main) {
            uploadImage.invoke(uri, username, email).observeForever {
                _chooseNameResult.postValue(it)
            }
        }
    }

}