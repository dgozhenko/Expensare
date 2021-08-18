package com.example.presentation.ui.dashboard

import androidx.lifecycle.*
import com.example.data.interactors.expenses.DownloadExpenses
import com.example.data.interactors.user.DownloadUser
import com.example.data.repositories.ExpenseRepository
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.daos.ExpenseDao
import com.example.domain.database.daos.UserDao
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.database.entities.UserEntity
import com.example.data.storage.Storage
import com.example.domain.database.entities.GroupEntity
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
    private val expenseRepository: ExpenseRepository,
    private val storage: Storage,
    private val downloadUser: DownloadUser,
    private val downloadExpenses: DownloadExpenses
) : ViewModel() {

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity>
        get() = _user

    private val _group = MutableLiveData<GroupEntity>()
    val group: LiveData<GroupEntity>
        get() = _group

    private val _expenses = MutableLiveData<ArrayList<ExpenseEntity>>()
    val expense: LiveData<ArrayList<ExpenseEntity>>
        get() = _expenses

    private val _refreshedExpenses = MutableLiveData<ArrayList<ExpenseEntity>>()
    val refreshedExpenses: LiveData<ArrayList<ExpenseEntity>>
        get() = _refreshedExpenses

    private val groupDao = database.groupDao()
    private val createUserDao: UserDao = database.userDao()
    private val expenseDao: ExpenseDao = database.expenseDao()

    init {
        syncWithFirebase()
    }

    private fun syncWithFirebase() {
        // Repos
        getUserInfo()
        // To-do
        getGroupByGroupId()
        // repos
        getGroupExpenses()
    }

     private fun getUserInfo() {
         viewModelScope.launch(Dispatchers.IO) {
             val downloadedUser = downloadUser.invoke()
             if (downloadedUser != UserEntity.EMPTY) {
                 _user.postValue(downloadedUser)
             } else {
                 _user.postValue(null)
             }
         }
    }

    private fun getGroupByGroupId() {
        val groupId = storage.groupId
        val reference =
            FirebaseDatabase.getInstance(
                    "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                )
                .getReference("/groups/")
        val groupsArray = arrayListOf<GroupEntity>()
            reference.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                val groupInfo = it.getValue(GroupEntity::class.java)
                                if (groupInfo != null) {
                                    if (groupInfo.groupUid == groupId) {
                                        groupsArray.add(groupInfo)
                                    }
                                }
                            }
                            viewModelScope.launch(Dispatchers.IO) {
                                groupDao.downloadAllGroups(groupsArray)

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )
    }

    fun refreshExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            val expenses = downloadExpenses.invoke()
            if(expenses.isNotEmpty()) {
                expenses.reverse()
                _refreshedExpenses.postValue(expenses)
            }
        }
    }

    private fun getGroupExpenses() {
        viewModelScope.launch(Dispatchers.IO){
            val expenses = downloadExpenses.invoke()
            if (expenses.isNotEmpty()) {
                expenses.reverse()
                _expenses.postValue(expenses)
            } else {
                _expenses.postValue(null)
            }
        }
    }
}
