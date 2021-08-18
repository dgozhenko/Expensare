package com.example.presentation.ui.dashboard

import androidx.lifecycle.*
import com.example.data.interactors.expenses.DownloadExpenses
import com.example.data.interactors.group.GetGroupByGroupId
import com.example.data.interactors.user.DownloadUser
import com.example.data.repositories.ExpenseRepository
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.daos.ExpenseDao
import com.example.domain.database.daos.UserDao
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.database.entities.UserEntity
import com.example.data.storage.Storage
import com.example.domain.database.entities.GroupEntity
import com.example.domain.models.Group
import com.example.domain.models.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    database: ExpensareDatabase,
    private val getGroupByGroupId: GetGroupByGroupId,
    private val storage: Storage,
    private val downloadUser: DownloadUser,
    private val downloadExpenses: DownloadExpenses
) : ViewModel() {

    private val _user = MutableLiveData<Response<UserEntity>>()
    val user: LiveData<Response<UserEntity>>
        get() = _user

    private val _group = MutableLiveData<Response<Group>>()
    val group: LiveData<Response<Group>>
        get() = _group

    private val _expenses = MutableLiveData<Response<ArrayList<ExpenseEntity>>>()
    val expense: LiveData<Response<ArrayList<ExpenseEntity>>>
        get() = _expenses

    private val _refreshedExpenses = MutableLiveData<Response<ArrayList<ExpenseEntity>>>()
    val refreshedExpenses: LiveData<Response<ArrayList<ExpenseEntity>>>
        get() = _refreshedExpenses

    init {
        getGroupByGroupId()
    }

     fun syncWithFirebase() {
        getGroupExpenses()
        // Repos
        getUserInfo()
    }

     private fun getUserInfo() {
         viewModelScope.launch(Dispatchers.Main) {
             downloadUser.invoke().observeForever {
              _user.postValue(it)
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

    fun refreshExpenses() {
        viewModelScope.launch(Dispatchers.Main) {
           downloadExpenses.invoke().observeForever {
                _refreshedExpenses.postValue(it)
           }
        }
    }

    private fun getGroupExpenses() {
        viewModelScope.launch(Dispatchers.Main){
            downloadExpenses.invoke().observeForever {
                _expenses.postValue(it)
            }
        }
    }
}
