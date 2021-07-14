package com.example.expensare.ui.dashboard

import androidx.lifecycle.*
import com.example.expensare.data.database.ExpensareDatabase
import com.example.expensare.data.database.daos.ExpenseDao
import com.example.expensare.data.database.daos.UserDao
import com.example.expensare.data.database.entities.ExpenseEntity
import com.example.expensare.data.database.entities.GroupEntity
import com.example.expensare.data.database.entities.UserEntity
import com.example.expensare.data.models.Expense
import com.example.expensare.data.models.Group
import com.example.expensare.data.models.User
import com.example.expensare.data.repositories.ExpenseRepository
import com.example.expensare.ui.storage.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel
@Inject
constructor(
    database: ExpensareDatabase,
    private val expenseRepository: ExpenseRepository,
    private val storage: Storage
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
        getUserInfo()
        getGroupByGroupId()
        getGroupExpenses()
        getUser()
        getGroups()
        getExpenses()
    }

     private fun getUserInfo() {
        val userId = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance(
                    "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                )
                .getReference("/users/")
        val usersArray = arrayListOf<UserEntity>()
        reference.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var userIsExist: Boolean = false
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val userInfo = it.getValue(UserEntity::class.java)
                            if (userInfo != null) {
                                if (userInfo.userUidId == userId) {
                                    userIsExist = true
                                    usersArray.add(userInfo)
                                }
                            }
                        }
                        if (!userIsExist) {
                            _user.postValue(null)
                        }
                        viewModelScope.launch(Dispatchers.IO) {
                                createUserDao.downloadUsers(usersArray)

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
        )
    }

    fun getGroupByGroupId() {
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
            val expenses = expenseRepository.getAllExpenses()
            if(expenses.isNotEmpty()) {
                expenses.reverse()
                _refreshedExpenses.postValue(expenses)
            }
        }
    }

    private fun getGroupExpenses() {
        val groupId = storage.groupId
        val expensesArrayList = arrayListOf<ExpenseEntity>()
        val reference =
            FirebaseDatabase.getInstance(
                    "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                )
                .getReference("/expenses/$groupId/")
            reference.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                val expense = it.getValue(ExpenseEntity::class.java)
                                if (expense != null) {
                                    expensesArrayList.add(expense)
                                } else {
                                    _expenses.postValue(null)
                                }
                            }
                            viewModelScope.launch(Dispatchers.IO) {
                                if (expensesArrayList.size == 0) {

                                } else {

                                    expenseDao.downloadExpenses(expensesArrayList)

                                }
                            }
                        } else {
                            _expenses.postValue(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )
    }

    private fun getExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            val expenses = expenseRepository.getAllExpenses()
            if (expenses.isNotEmpty()) {
                expenses.reverse()
                _expenses.postValue(expenses)
            }

        }
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val returnedUser = createUserDao.getAllUsers()
            if (returnedUser.isNotEmpty()) {
                _user.postValue(returnedUser.first())
            }
        }
    }

    private fun getGroups() {
        viewModelScope.launch(Dispatchers.IO) {
            val returnedGroup = groupDao.getGroups()
            if (returnedGroup.isNotEmpty()) {
                _group.postValue(returnedGroup.first())
            }
        }
    }
}
