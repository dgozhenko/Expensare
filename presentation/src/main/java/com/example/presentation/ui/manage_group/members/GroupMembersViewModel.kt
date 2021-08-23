package com.example.presentation.ui.manage_group.members

import androidx.lifecycle.*
import com.example.data.interactors.group.*
import com.example.domain.models.Group
import com.example.data.storage.Storage
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User
import com.example.domain.models.util.Response
import com.example.domain.models.util.UserGroupData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class GroupMembersViewModel @Inject constructor(private val getGroupByGroupId: GetGroupByGroupId,
                                                private val getUserByEmail: GetUserByEmail,
                                                private val createGroupInvite: CreateGroupInvite): ViewModel() {

    private val _user = MutableLiveData<ArrayList<User>>()
    val user: LiveData<ArrayList<User>>
        get() = _user

    private val _inviteResult = MutableLiveData<Response<String>>()
    val inviteResult: LiveData<Response<String>> get() = _inviteResult

    private val _userByEmail = MutableLiveData<Response<User>>()
    val userByEmail: LiveData<Response<User>> get() = _userByEmail

    private val _group = MutableLiveData<Response<Group>>()
    val group: LiveData<Response<Group>> get() = _group

    init {
        getGroupByGroupId()
    }

    private fun getGroupByGroupId() {
        viewModelScope.launch(Dispatchers.Main) {
            getGroupByGroupId.invoke().observeForever {
                _group.postValue(it)
            }
        }
    }

    fun getUserByEmail(email: String) {
        viewModelScope.launch(Dispatchers.Main) {
            getUserByEmail.invoke(email).observeForever {
                _userByEmail.postValue(it)
            }
        }
    }

    fun sendInvite(user: User, group: Group) {
        viewModelScope.launch(Dispatchers.Main) {
            val currentDateTime = LocalDateTime.now()
            val date = currentDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"))
            createGroupInvite.invoke(user = user, group = group, dateTime = date).observeForever {
                _inviteResult.postValue(it)
            }
        }
    }

     fun getUsersFromGroup(group: Group) {
        _user.postValue(group.users)
    }

}