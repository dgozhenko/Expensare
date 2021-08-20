package com.example.presentation.ui.manage_group.members

import androidx.lifecycle.*
import com.example.data.interactors.group.AddUserToGroup
import com.example.data.interactors.group.CreateUserInGroup
import com.example.data.interactors.group.GetGroupByGroupId
import com.example.data.interactors.group.GetUserByEmail
import com.example.domain.models.Group
import com.example.data.storage.Storage
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User
import com.example.domain.models.util.Response
import com.example.domain.models.util.UserGroupData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupMembersViewModel @Inject constructor(private val storage: Storage,
                                                private val getGroupByGroupId: GetGroupByGroupId,
                                                private val addUserToGroup: AddUserToGroup,
                                                private val getUserByEmail: GetUserByEmail,
                                                private val createUserInGroup: CreateUserInGroup): ViewModel() {

    private val _user = MutableLiveData<ArrayList<User>>()
    val user: LiveData<ArrayList<User>>
        get() = _user

    private val _addUserToGroupResult = MutableLiveData<Response<UserGroupData>>()
    val addUserToGroupResult: LiveData<Response<UserGroupData>>
        get() = _addUserToGroupResult

    private val _createUserInGroupResult = MutableLiveData<Response<String>>()
    val createUserInGroupResult: LiveData<Response<String>>
        get() = _createUserInGroupResult

    private val _userByEmail = MutableLiveData<Response<User>>()
    val userByEmail: LiveData<Response<User>> get() = _userByEmail

    private val _group = MutableLiveData<Response<Group>>()
    val group: LiveData<Response<Group>> get() = _group

    init {
        getGroupByGroupId()
    }
    // TODO: 17.08.2021 Fragment
    private fun getGroupByGroupId() {
        viewModelScope.launch(Dispatchers.Main) {
            getGroupByGroupId.invoke().observeForever {
                _group.postValue(it)
            }
        }
    }

    // TODO: 17.08.2021 Repository
    fun addUserToGroup(user: User) {
        viewModelScope.launch(Dispatchers.Main) {
            addUserToGroup.invoke(user).observeForever {
                _addUserToGroupResult.postValue(it)
            }
        }
    }

    // TODO: 17.08.2021 Repository
     fun createUserInGroup(userGroupData: UserGroupData) {
        viewModelScope.launch(Dispatchers.Main) {
            createUserInGroup.invoke(userGroupData).observeForever {
                _createUserInGroupResult.postValue(it)
            }
        }
    }

    // TODO: 17.08.2021 Repository
    fun getUserByEmail(email: String) {
        viewModelScope.launch(Dispatchers.Main) {
            getUserByEmail.invoke(email).observeForever {
                _userByEmail.postValue(it)
            }
        }
    }

    // TODO: 17.08.2021 Fragment
     fun getUsersFromGroup(group: Group) {
        _user.postValue(group.users)
    }

}