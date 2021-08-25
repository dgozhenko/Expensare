package com.example.presentation.ui.group

import androidx.lifecycle.*
import com.example.data.interactors.group.GetAllGroups
import com.example.data.interactors.group.GetAllInvites
import com.example.data.interactors.group.ListenForGroups
import com.example.data.interactors.user.DownloadUser
import com.example.domain.models.Group
import com.example.data.storage.Storage
import com.example.domain.models.GroupInvite
import com.example.domain.models.GroupInvites
import com.example.domain.models.User
import com.example.domain.models.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseGroupViewModel @Inject constructor(private val storage: Storage,
                                               private val downloadUser: DownloadUser,
                                               private val listenForGroups: ListenForGroups,
                                               private val getAllGroups: GetAllGroups,
                                               private val getAllInvites: GetAllInvites): ViewModel() {


    private val _groups = MutableLiveData<Response<ArrayList<Group>>>()
    val groups: LiveData<Response<ArrayList<Group>>> get() = _groups

    private val _groupIds = MutableLiveData<Response<ArrayList<String>>>()
    val groupIds: LiveData<Response<ArrayList<String>>> get() = _groupIds

    private val _user = MutableLiveData<Response<User>>()
    val user: LiveData<Response<User>>
        get() = _user

    private val _invites = MutableLiveData<Response<GroupInvites>>()
    val invites: LiveData<Response<GroupInvites>> get() = _invites

    fun saveGroupID(groupId: String) {
        storage.groupId = groupId
    }

    init {
        getUserInfo()
    }

    fun getAllInvites(user: User) {
        viewModelScope.launch(Dispatchers.Main) {
            getAllInvites.invoke(user).observeForever {
                _invites.postValue(it)
            }
        }
    }

    fun listenForGroups(user: User) {
        viewModelScope.launch(Dispatchers.Main) {
            listenForGroups.invoke(user).observeForever {
                _groupIds.postValue(it)
            }
        }
    }

    fun getAllGroups(groupIds: ArrayList<String>) {
        viewModelScope.launch(Dispatchers.Main) {
            getAllGroups.invoke(groupIds).observeForever {
                _groups.postValue(it)
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