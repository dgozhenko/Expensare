package com.example.expensare.ui.auth.registration

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RegistrationViewModel(application: Application): AndroidViewModel(application) {

    private val _isRegisterComplete = MutableLiveData<Boolean>()
    val isRegisterComplete: LiveData<Boolean> get() = _isRegisterComplete

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> get() = _error

    fun registerUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _isRegisterComplete.postValue(true)
                        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                        FirebaseAuth.getInstance().signOut()
                    } else {
                        return@addOnCompleteListener
                    }
                }
                .addOnFailureListener {
                    _error.postValue(it)
                    return@addOnFailureListener
                }
        }
    }

    fun registerCompleted() {
        _isRegisterComplete.postValue(null)
    }

    fun errorCompleted() {
        _error.postValue(null)
    }
}