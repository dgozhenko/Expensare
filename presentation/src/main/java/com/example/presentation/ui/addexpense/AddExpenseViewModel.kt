package com.example.presentation.ui.addexpense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.interactors.debt.CreateDebt
import com.example.data.interactors.expenses.CreateExpense
import com.example.data.interactors.group.GetAllUsersFromGroup
import com.example.data.interactors.group.GetGroupByGroupId
import com.example.data.interactors.user.DownloadUser
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.models.UserDebt
import com.example.data.storage.Storage
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class AddExpenseResult {
    object Success : AddExpenseResult()
    data class Error(val exception: String) : AddExpenseResult()
}

sealed class AddDebtResult {
    object Success : AddDebtResult()
    data class Error(val exception: Exception) : AddDebtResult()
}

@HiltViewModel
class AddExpenseViewModel
@Inject
constructor(private val storage: Storage, database: ExpensareDatabase,
            private val downloadUser: DownloadUser,
            private val createExpense: CreateExpense,
            private val createDebt: CreateDebt,
            private val getAllUsersFromGroup: GetAllUsersFromGroup,
            private val getGroupByGroupId: GetGroupByGroupId) : ViewModel() {

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity>
        get() = _user

    private val _addExpenseResult = MutableLiveData<AddExpenseResult>()
    val addExpenseResult: LiveData<AddExpenseResult>
        get() = _addExpenseResult

    private val _addDebtResult = MutableLiveData<AddDebtResult>()
    val addDebtResult: LiveData<AddDebtResult>
        get() = _addDebtResult

    private val _group = MutableLiveData<Group>()
    val group: LiveData<Group>
        get() = _group

    private val _users = MutableLiveData<ArrayList<UserEntity>>()
    val users: LiveData<ArrayList<UserEntity>>
        get() = _users

    init {
        getUser()
        getGroups()
    }
    // TODO: 17.08.2021 Repository
    fun createDebt(amount: Int, fromUser: UserEntity, toUser: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val debt = UserDebt(toUser, fromUser, amount, amount, false)
            createDebt.invoke(debt)
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
            val result = createExpense.invoke(expenseEntity)
            _addExpenseResult.postValue(AddExpenseResult.Success)

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
            val group = getGroupByGroupId.invoke()
            _group.postValue(group)
        }
    }
    // TODO: 17.08.2021 Repository
    // SecondUser ??????
    fun getUsersFromGroup(group: Group) {
        val myUid = FirebaseAuth.getInstance().uid
        val filteredUserArrayList = arrayListOf<UserEntity>()
        viewModelScope.launch(Dispatchers.IO) {
            val usersFromGroup = getAllUsersFromGroup.invoke(group)
            usersFromGroup.forEach {
                if (it.userUidId != myUid) {
                    filteredUserArrayList.add(it)
                }
            }
            _users.postValue(filteredUserArrayList)
        }
    }
}
