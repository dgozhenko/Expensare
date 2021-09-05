package com.example.presentation.ui.mydebts.create_debt

import androidx.lifecycle.*
import com.example.data.interactors.group.GetAllUsersFromGroup
import com.example.data.interactors.group.GetGroupByGroupId
import com.example.data.interactors.manual_debts.CreateDebt
import com.example.data.interactors.user.DownloadUser
import com.example.domain.models.*
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class CreateDebtViewModel @Inject constructor(
    private val getGroupByGroupId: GetGroupByGroupId,
    private val downloadUser: DownloadUser,
    private val createDebtUseCase: CreateDebt,
    private val getAllUsersFromGroup: GetAllUsersFromGroup
) : ViewModel() {

    private val _user = MutableLiveData<Response<User>>()
    val user: LiveData<Response<User>>
        get() = _user

    private val _group = MutableLiveData<Response<Group>>()
    val group: LiveData<Response<Group>>
        get() = _group

    private var _users = ArrayList<User>()
    val users: ArrayList<User>
        get() = _users

    private val _createDebtLiveData: SingleLiveEvent<Response<String>> = SingleLiveEvent()
    val createDebtLiveData: LiveData<Response<String>>
        get() = _createDebtLiveData


    init {
        getUserInfo()
        getGroupByGroupId()
    }

    // TODO: 17.08.2021 Repository
    private fun getUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            downloadUser.invoke().observeForever { _user.postValue(it) }
        }
    }

    // TODO: 17.08.2021 Repository
    private fun getGroupByGroupId() {
        viewModelScope.launch(Dispatchers.Main) {
            getGroupByGroupId.invoke().observeForever { _group.postValue(it) }
        }
    }

    // TODO: 17.08.2021 Repository
    fun createDebt(debtFor: String, amount: Int, fromUser: User, toUser: User) {
        viewModelScope.launch(Dispatchers.Main) {
            createDebtUseCase.invoke(debtFor, amount, fromUser, toUser).observeForever { _createDebtLiveData.postValue(it) }
        }
    }

      fun getUsersFromGroup(group: Group) {
          _users = group.users
    }
}