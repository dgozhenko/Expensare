package com.example.presentation.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.interactors.expenses.DownloadExpenses
import com.example.data.interactors.group.GetGroupByGroupId
import com.example.data.interactors.user.DownloadUser
import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Expense
import com.example.domain.models.Group
import com.example.domain.models.User
import com.example.domain.models.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel
@Inject
constructor(
    database: ExpensareDatabase,
    private val getGroupByGroupId: GetGroupByGroupId,
    private val storage: Storage,
    private val downloadUser: DownloadUser,
    private val downloadExpenses: DownloadExpenses
) : ViewModel() {

    private val _user = MutableLiveData<Response<User>>()
    val user: LiveData<Response<User>>
        get() = _user

    private val _group = MutableLiveData<Response<Group>>()
    val group: LiveData<Response<Group>>
        get() = _group

    private val _expenses = MutableLiveData<Response<ArrayList<Expense>>>()
    val expense: LiveData<Response<ArrayList<Expense>>>
        get() = _expenses

    private val _refreshedExpenses = MutableLiveData<Response<ArrayList<Expense>>>()
    val refreshedExpenses: LiveData<Response<ArrayList<Expense>>>
        get() = _refreshedExpenses

    private val _groupId = MutableLiveData<String>()
    val groupId: LiveData<String> get() = _groupId

    init {
        getStoredGroupId()
        getUserInfo()
    }



     private fun getUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            downloadUser.invoke().observeForever { _user.postValue(it) }
        }
    }

     fun getGroupByGroupId() {
        viewModelScope.launch(Dispatchers.Main) {
            getGroupByGroupId.invoke().observeForever { _group.postValue(it) }
        }
    }

    fun refreshExpenses() {
        viewModelScope.launch(Dispatchers.Main) {
            downloadExpenses.invoke().observeForever { _refreshedExpenses.postValue(it) }
        }
    }

     fun getGroupExpenses() {
        viewModelScope.launch(Dispatchers.Main) {
            downloadExpenses.invoke().observeForever { _expenses.postValue(it) }
        }
    }

    private fun getStoredGroupId() {
        _groupId.postValue(storage.groupId)
    }

    fun deleteStoredGroupId() {
        storage.groupId = "def"
    }
}
