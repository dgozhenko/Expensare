package com.example.presentation.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.interactors.group.GetGroupByGroupId
import com.example.data.interactors.user.DownloadUser
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User
import com.example.domain.models.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val getGroupByGroupId: GetGroupByGroupId,
    private val downloadUser: DownloadUser
    ) : ViewModel() {

    private val _user = MutableLiveData<Response<User>>()
    val user: LiveData<Response<User>>
        get() = _user


    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            downloadUser.invoke().observeForever {
                _user.postValue(it)
            }
        }
    }
}