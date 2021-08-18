package com.example.presentation.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.interactors.auth.LoginUser
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUserUseCase: LoginUser) : ViewModel() {
  private val _userLiveData: SingleLiveEvent<Response<String>> = SingleLiveEvent()
  val userLiveData: LiveData<Response<String>>
    get() = _userLiveData

  // TODO: 17.08.2021 Repository
  fun loginUser(email: String, password: String) {
    viewModelScope.launch(Dispatchers.Main) {
      loginUserUseCase(email, password).observeForever { _userLiveData.postValue(it) }
    }
  }
}
