package com.example.presentation.ui.auth.login

import androidx.lifecycle.*
import com.example.domain.database.entities.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _userLiveData = MutableLiveData<Boolean>()
    val userLiveData: LiveData<Boolean>
        get() = _userLiveData

    private val _verificationError = MutableLiveData<Boolean>()
    val verificationError: LiveData<Boolean>
        get() = _verificationError

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception>
        get() = _error

    fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _error.postValue(null)
                    if (task.isSuccessful) {
                        if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                            _verificationError.postValue(false)
                            val uid = FirebaseAuth.getInstance().uid
                            val reference =
                                FirebaseDatabase.getInstance(
                                        "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                                    )
                                    .getReference("/users/")

                            reference.addListenerForSingleValueEvent(
                                object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            var userIsExisted = false
                                            snapshot.children.forEach {
                                                val user = it.getValue(UserEntity::class.java)
                                                if (user != null) {
                                                    if (user.userUidId == uid) {
                                                        userIsExisted = true
                                                    }
                                                }
                                            }

                                            if (userIsExisted) {
                                                _userLiveData.postValue(true)
                                            } else {
                                                _userLiveData.postValue(false)
                                            }
                                        } else {
                                            _userLiveData.postValue(false)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                }
                            )
                        } else {
                            _verificationError.postValue(true)
                            FirebaseAuth.getInstance().signOut()
                        }
                    }
                }
                .addOnFailureListener {
                    _error.postValue(it)
                }
        }
    }

    fun errorComplete() {
        _error.value = null
    }

    fun verificationErrorComplete() {
        _verificationError.value = null
    }
}
