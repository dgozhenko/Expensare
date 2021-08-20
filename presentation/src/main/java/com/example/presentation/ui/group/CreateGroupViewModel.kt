package com.example.presentation.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.interactors.group.CreateGroup
import com.example.data.interactors.user.DownloadUser
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import com.example.domain.models.UserDebt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(private val downloadUser: DownloadUser,
                                               private val createGroup: CreateGroup) : ViewModel() {

    private val _user = MutableLiveData<Response<UserEntity>>()
    val user: LiveData<Response<UserEntity>>
        get() = _user

    private val _createGroupResult = MutableLiveData<Response<String>>()
    val createGroupResult: LiveData<Response<String>>
        get() = _createGroupResult

    init {
        getUserInfo()
    }

    // TODO: 17.08.2021 Repository
    fun createGroup(groupName: String, groupType: String, userEntity: UserEntity) {
        val users = arrayListOf<UserEntity>()
        val groupId = UUID.randomUUID().toString()
        users.add(userEntity)
        val group = Group(groupID = groupId, groupName = groupName, groupType = groupType, users = users)
        viewModelScope.launch(Dispatchers.Main) {
            createGroup.invoke(groupId = groupId, group = group).observeForever {
                _createGroupResult.postValue(it)
            }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
           downloadUser.invoke().observeForever {
            _user.postValue(it)
           }
        }
    }
}
