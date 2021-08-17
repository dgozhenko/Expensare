package com.example.presentation.ui.addexpense

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.interactors.expenses.CreateExpense
import com.example.data.interactors.expenses.DownloadExpenses
import com.example.data.interactors.user.DownloadUser
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.database.entities.GroupEntity
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.models.User
import com.example.domain.models.UserDebt
import com.example.data.storage.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class AddExpenseResult {
    object Success : AddExpenseResult()
    data class Error(val exception: Exception) : AddExpenseResult()
}

sealed class AddDebtResult {
    object Success : AddDebtResult()
    data class Error(val exception: Exception) : AddDebtResult()
}

@HiltViewModel
class AddExpenseViewModel
@Inject
constructor(private val storage: Storage, database: ExpensareDatabase, private val downloadUser: DownloadUser, private val createExpense: CreateExpense) : ViewModel() {

    private val createUserDao = database.userDao()
    private val groupDao = database.groupDao()
    private val expenseDao = database.expenseDao()

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity>
        get() = _user

    private val _addExpenseResult = MutableLiveData<AddExpenseResult>()
    val addExpenseResult: LiveData<AddExpenseResult>
        get() = _addExpenseResult

    private val _addDebtResult = MutableLiveData<AddDebtResult>()
    val addDebtResult: LiveData<AddDebtResult>
        get() = _addDebtResult

    private val _group = MutableLiveData<GroupEntity>()
    val group: LiveData<GroupEntity>
        get() = _group

    private val _users = MutableLiveData<ArrayList<User>>()
    val users: LiveData<ArrayList<User>>
        get() = _users

    init {
        getUser()
        getGroups()
    }

    fun createDebt(amount: Int, fromUser: User, toUser: User) {
        val groupId = storage.groupId
        var operationDone = false
        viewModelScope.launch(Dispatchers.IO) {
            val reference =
                FirebaseDatabase.getInstance(
                        "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                    )
                    .getReference("group_debts/$groupId")
                    .push()
            val referenceCheck =
                FirebaseDatabase.getInstance(
                        "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                    )
                    .getReference("group_debts/$groupId/")
            val referenceAdd =
                FirebaseDatabase.getInstance(
                        "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                    )
                    .getReference("group_debts/$groupId")
            referenceCheck.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                val key = it.key
                                val userDebt = it.getValue(UserDebt::class.java)!!
                                if (userDebt.firstUser == toUser && userDebt.secondUser == fromUser
                                ) {
                                    operationDone = true
                                    referenceAdd
                                        .child("/$key/")
                                        .setValue(
                                            UserDebt(
                                                userDebt.firstUser,
                                                userDebt.secondUser,
                                                userDebt.firstUserAmount + amount,
                                                userDebt.secondUserAmount - amount,
                                                false
                                            )
                                        )
                                    _addDebtResult.postValue(AddDebtResult.Success)
                                } else if (userDebt.firstUser == fromUser &&
                                        userDebt.secondUser == toUser
                                ) {
                                    operationDone = true
                                    referenceAdd
                                        .child("/$key/")
                                        .setValue(
                                            UserDebt(
                                                userDebt.firstUser,
                                                userDebt.secondUser,
                                                userDebt.firstUserAmount - amount,
                                                userDebt.secondUserAmount + amount,
                                                false
                                            )
                                        )
                                    _addDebtResult.postValue(AddDebtResult.Success)
                                }
                            }
                            if (!operationDone) {
                                reference.setValue(
                                    UserDebt(toUser, fromUser, amount, -amount, false)
                                )
                            }
                        } else {
                            reference.setValue(UserDebt(toUser, fromUser, amount, -amount, false))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )
        }
    }

    fun createExpense(name: String, amount: Int, user: UserEntity, connection: Boolean) {
        val groupId = storage.groupId
        val pattern = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val newCalendar = Calendar.getInstance(Locale.getDefault())
        val neededDate = simpleDateFormat.format(newCalendar.time)
        val expenseId = UUID.randomUUID().toString()

        val expenseEntity = ExpenseEntity(expenseId, name, amount, user, groupId, neededDate, true)
        viewModelScope.launch(Dispatchers.IO) {
            val exception = createExpense.invoke(expenseEntity)
            if (exception == null) {
                _addExpenseResult.postValue(AddExpenseResult.Success)
            } else {
                _addExpenseResult.postValue(AddExpenseResult.Error(exception))
            }

        }

    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val returnedUser = downloadUser.invoke()
            if (returnedUser != UserEntity.EMPTY) {
                _user.postValue(returnedUser)
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

    fun getUsersFromGroup(group: Group) {
        val userId = FirebaseAuth.getInstance().uid
        val userIdArrayList = arrayListOf<String>()
        val userArrayList = arrayListOf<User>()
        group.users.forEach {
            if (userId != it) {
                userIdArrayList.add(it)
            }
        }
        userIdArrayList.forEach { currentUserId ->
            val reference =
                FirebaseDatabase.getInstance(
                        "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                    )
                    .getReference("/users/")
            viewModelScope.launch(Dispatchers.IO) {
                reference.addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                snapshot.children.forEach {
                                    val userInfo = it.getValue(User::class.java)
                                    if (userInfo != null) {
                                        if (userInfo.uid == currentUserId) {
                                            userArrayList.add(userInfo)
                                        }
                                    }
                                }
                                _users.postValue(userArrayList)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    }
                )
            }
        }
    }
}
