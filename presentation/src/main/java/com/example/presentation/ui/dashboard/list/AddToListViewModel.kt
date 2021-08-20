package com.example.presentation.ui.dashboard.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.interactors.group.GetGroupByGroupId
import com.example.data.interactors.list.CreateListItem
import com.example.data.interactors.user.DownloadUser
import com.example.domain.models.Group
import com.example.domain.models.GroupList
import com.example.data.storage.Storage
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User
import com.example.domain.models.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

sealed class AddToListResult {
    object Success : AddToListResult()
    data class Error(val exception: Exception) : AddToListResult()
}

@HiltViewModel
class AddToListViewModel @Inject constructor(private val storage: Storage,
                                            private val getGroupByGroupId: GetGroupByGroupId,
                                            private val createListItem: CreateListItem,
                                            private val downloadUser: DownloadUser): ViewModel() {

    private val _user = MutableLiveData<Response<User>>()
    val user: LiveData<Response<User>>
        get() = _user

    private val _addToListResult = MutableLiveData<Response<String>>()
    val addToListResult: LiveData<Response<String>>
        get() = _addToListResult

    private val _group = MutableLiveData<Response<Group>>()
    val group: LiveData<Response<Group>>
        get() = _group

    private val _users = MutableLiveData<ArrayList<User>>()
    val users: LiveData<ArrayList<User>>
        get() = _users

    init {
        getUserInfo()
        // TODO: 17.08.2021 Repository
        getGroupByGroupId()
    }
    // TODO: 17.08.2021 Repository
    fun createListItem(category: String, name: String, user: User, quantity: Int, store: String) {
        val listItem = GroupList(store = store, quantity = quantity, name = name, type = category, false, user = user)
        viewModelScope.launch(Dispatchers.Main) {
            createListItem.invoke(listItem).observeForever {
                _addToListResult.postValue(it)
            }
        }
    }

    private fun getGroupByGroupId() {
        viewModelScope.launch(Dispatchers.Main) {
            getGroupByGroupId.invoke().observeForever {
                _group.postValue(it)
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

    fun getUsersFromGroup(group: Group) {
        _users.postValue(group.users)

    }



}