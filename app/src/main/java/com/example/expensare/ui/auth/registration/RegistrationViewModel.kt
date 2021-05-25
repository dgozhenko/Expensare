package com.example.expensare.ui.auth.registration

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel(application: Application): AndroidViewModel(application) {

    private val _isRegisterComplete = MutableLiveData<Boolean>()
    val isRegisterComplete: LiveData<Boolean> get() = _isRegisterComplete

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
                    _isRegisterComplete.postValue(false)
                    Toast.makeText(getApplication<Application>().baseContext, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun registerCompleted() {
        _isRegisterComplete.postValue(null)
    }
}