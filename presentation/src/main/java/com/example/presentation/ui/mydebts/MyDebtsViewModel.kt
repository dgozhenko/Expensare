package com.example.presentation.ui.mydebts

import androidx.lifecycle.*
import com.example.data.interactors.group.GetGroupByGroupId
import com.example.data.interactors.manual_debts.CreateRequest
import com.example.data.interactors.manual_debts.GetLentDebts
import com.example.data.interactors.manual_debts.GetOweDebts
import com.example.data.interactors.user.DownloadUser
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.database.entities.ManualDebtEntity
import com.example.domain.models.Debt
import com.example.domain.models.User
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MyDebtsViewModel @Inject constructor(
    private val getGroupByGroupId: GetGroupByGroupId,
    private val downloadUser: DownloadUser,
    private val getLentDebts: GetLentDebts,
    private val getOweDebts: GetOweDebts,
    private val createRequest: CreateRequest
) :
    ViewModel() {

    private val _user = MutableLiveData<Response<User>>()
    val user: LiveData<Response<User>>
        get() = _user

    private val _group = MutableLiveData<Response<Group>>()
    val group: LiveData<Response<Group>>
        get() = _group

    private val _lentDebts = MutableLiveData<Response<ArrayList<Debt>>>()
    val lentDebts: LiveData<Response<ArrayList<Debt>>>
        get() = _lentDebts

    private val _oweDebts = MutableLiveData<Response<ArrayList<Debt>>>()
    val oweDebts: LiveData<Response<ArrayList<Debt>>>
        get() = _oweDebts

    private val _refreshedLentDebts = MutableLiveData<Response<ArrayList<Debt>>>()
    val refreshedLentDebts: LiveData<Response<ArrayList<Debt>>>
        get() = _refreshedLentDebts

    private val _refreshedOweDebts = MutableLiveData<Response<ArrayList<Debt>>>()
    val refreshedOweDebts: LiveData<Response<ArrayList<Debt>>>
        get() = _refreshedOweDebts

    private val _createRequestLiveData: SingleLiveEvent<Response<String>> = SingleLiveEvent()
    val createRequestLiveData: LiveData<Response<String>>
        get() = _createRequestLiveData

    init {
        getUserInfo()
        getGroupByGroupId()
        getLentDebts()
        getOweDebts()
        refreshLentDebts()
        refreshOweDebts()
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
    private fun getLentDebts() {
        viewModelScope.launch(Dispatchers.Main) {
            getLentDebts.invoke().observeForever { _lentDebts.postValue(it) }
        }
    }

    // TODO: 17.08.2021 Repository
    private fun getOweDebts() {
        viewModelScope.launch(Dispatchers.Main) {
            getOweDebts.invoke().observeForever { _oweDebts.postValue(it) }
        }
    }

    private fun refreshLentDebts() {
        viewModelScope.launch(Dispatchers.Main) {
            getLentDebts.invoke().observeForever { _refreshedLentDebts.postValue(it) }
        }
    }

    private fun refreshOweDebts() {
        viewModelScope.launch(Dispatchers.Main) {
            getOweDebts.invoke().observeForever { _refreshedOweDebts.postValue(it) }
        }
    }

    fun createRequest(debt: Debt) {
        viewModelScope.launch(Dispatchers.Main) {
            createRequest.invoke(debt).observeForever { _createRequestLiveData.postValue(it) }
        }
    }
}