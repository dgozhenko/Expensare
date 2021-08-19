package com.example.presentation.ui.auth.registration

import androidx.lifecycle.*
import com.example.data.interactors.auth.RegisterUser
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val registerUserUseCase: RegisterUser) :
    ViewModel() {

    private val _userLiveData: SingleLiveEvent<Response<String>> = SingleLiveEvent()
    val userLiveData: LiveData<Response<String>>
        get() = _userLiveData

    // TODO: 17.08.2021 Repository
    fun registerUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.Main) {
            registerUserUseCase(email, password).observeForever { _userLiveData.postValue(it) }
        }
    }
}