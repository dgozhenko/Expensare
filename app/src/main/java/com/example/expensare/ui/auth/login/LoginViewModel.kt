package com.example.expensare.ui.auth.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensare.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
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
                            val uid = FirebaseAuth.getInstance().uid ?: ""
                            val reference =
                                FirebaseDatabase.getInstance(
                                        "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                                    )
                                    .getReference("/users/")
                            var userIsExisted = false
                            reference.addListenerForSingleValueEvent(
                                object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            snapshot.children.forEach {
                                                val user = it.getValue(User::class.java)
                                                if (user != null) {
                                                    if (user.uid == uid) {
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
    fun loginComplete() {
        _userLiveData.value = null
    }

    fun errorComplete() {
        _error.value = null
    }

    fun verificationErrorComplete() {
        _verificationError.value = null
    }
}
