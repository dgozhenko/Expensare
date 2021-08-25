package com.example.presentation.ui.group.invite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.interactors.group.AddUserToGroup
import com.example.data.interactors.group.CreateUserInGroup
import com.example.data.interactors.group.DeclineInvite
import com.example.data.interactors.user.DownloadUser
import com.example.domain.models.Group
import com.example.domain.models.User
import com.example.domain.models.util.Response
import com.example.domain.models.util.UserGroupData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class GroupInvitesViewModel @Inject constructor(private val addUserToGroup: AddUserToGroup,
                                                private val createUserInGroup: CreateUserInGroup,
                                                private val downloadUser: DownloadUser,
                                                private val declineInvite: DeclineInvite): ViewModel() {

    private val _addUserResult = MutableLiveData<Response<UserGroupData>>()
    val addUserResult: LiveData<Response<UserGroupData>> get() = _addUserResult

    private val _createUserResult = MutableLiveData<Response<String>>()
    val createUserResult: LiveData<Response<String>> get() = _createUserResult

    private val _declineInviteResult = MutableLiveData<Response<String>>()
    val declineInviteResult: LiveData<Response<String>> get() = _declineInviteResult

    private val _user = MutableLiveData<Response<User>>()
    val user: LiveData<Response<User>> get() = _user

    init {
        downloadUser()
    }

     fun addUserToGroup(user : User, group: Group) {
        viewModelScope.launch(Dispatchers.Main) {
            addUserToGroup.invoke(user, group).observeForever {
                _addUserResult.postValue(it)
            }
        }
    }

    fun createUserInGroup(userGroupData: UserGroupData) {
        viewModelScope.launch(Dispatchers.Main) {
            createUserInGroup.invoke(userGroupData).observeForever {
                _createUserResult.postValue(it)
            }
        }
    }

    private fun downloadUser() {
        viewModelScope.launch(Dispatchers.Main) {
            downloadUser.invoke().observeForever {
                _user.postValue(it)
            }
        }
    }

    fun declineInvite(user: User, group: Group) {
        viewModelScope.launch(Dispatchers.Main) {
            declineInvite.invoke(user, group).observeForever {
                _declineInviteResult.postValue(it)
            }
        }
    }
}