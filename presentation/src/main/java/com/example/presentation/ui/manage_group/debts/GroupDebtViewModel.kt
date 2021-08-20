package com.example.presentation.ui.manage_group.debts

import androidx.lifecycle.*
import com.example.data.interactors.group.GetGroupByGroupId
import com.example.data.interactors.group.GetGroupDebts
import com.example.data.interactors.group.GetGroupDetailedDebt
import com.example.data.interactors.user.DownloadUser
import com.example.domain.models.Group
import com.example.domain.models.GroupDebt
import com.example.data.storage.Storage
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User
import com.example.domain.models.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.collections.ArrayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDebtViewModel @Inject constructor(private val storage: Storage,
                                             private val getGroupDebts: GetGroupDebts,
                                             private val getGroupDetailedDebt: GetGroupDetailedDebt,
                                             private val getGroupByGroupId: GetGroupByGroupId,
                                             private val downloadUser: DownloadUser) : ViewModel() {

    private val _group = MutableLiveData<Response<Group>>()
    val group: LiveData<Response<Group>>
        get() = _group

    private val _user = MutableLiveData<Response<User>>()
    val user: LiveData<Response<User>>
        get() = _user

    private val _users = MutableLiveData<ArrayList<User>>()
    val users: LiveData<ArrayList<User>>
        get() = _users

    private val _userDebt = MutableLiveData<Response<ArrayList<GroupDebt>>>()
    val groupDebt: LiveData<Response<ArrayList<GroupDebt>>>
        get() = _userDebt

    private val _detailedUserDebt = MutableLiveData<Response<ArrayList<GroupDebt>>>()
    val detailedGroupDebt: LiveData<Response<ArrayList<GroupDebt>>>
        get() = _detailedUserDebt

    init {
        getGroupByGroupId()
        getUserInfo()
    }

    fun getDetailedDebts(user: User, debtToMe: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            getGroupDetailedDebt.invoke(user = user, debtToMe = debtToMe).observeForever {
                _detailedUserDebt.postValue(it)
            }
        }
    }

    fun getDebts(user: ArrayList<User>, debtToMe: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            getGroupDebts.invoke(user, debtToMe).observeForever {
                _userDebt.postValue(it)
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
