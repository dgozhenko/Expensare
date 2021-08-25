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
import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Expense
import com.example.domain.models.Group
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import com.example.domain.models.GroupDebt
import com.example.domain.models.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class AddDebtResult {
    object Success : AddDebtResult()
    data class Error(val exception: Exception) : AddDebtResult()
}

@HiltViewModel
class AddExpenseViewModel
@Inject
constructor(
    private val storage: Storage,
    database: ExpensareDatabase,
    private val downloadUser: DownloadUser,
    private val createExpense: CreateExpense,
    private val createDebt: CreateDebt,
    private val getAllUsersFromGroup: GetAllUsersFromGroup,
    private val getGroupByGroupId: GetGroupByGroupId
) : ViewModel() {

    private val _user = MutableLiveData<Response<User>>()
    val user: LiveData<Response<User>>
        get() = _user

    private val _addExpenseResult = MutableLiveData<Response<String>>()
    val addExpenseResult: LiveData<Response<String>>
        get() = _addExpenseResult

    private val _addDebtResult: SingleLiveEvent<Response<String>> = SingleLiveEvent()
    val addDebtResult: LiveData<Response<String>>
        get() = _addDebtResult

    private val _group = MutableLiveData<Response<Group>>()
    val group: LiveData<Response<Group>>
        get() = _group

    private val _users = MutableLiveData<ArrayList<User>>()
    val users: LiveData<ArrayList<User>>
        get() = _users

    init {
        getUser()
        getGroups()
    }

    fun createDebt(amount: Int, oweUser: User, lentUser: User) {
        val debt = GroupDebt(lentUser, oweUser, amount, amount, expanded = false, true)
        viewModelScope.launch(Dispatchers.Main) {
            createDebt.invoke(debt).observeForever { _addDebtResult.postValue(it) }
        }
    }

    fun createExpense(name: String, amount: Int, user: User, connection: Boolean) {
        val groupId = storage.groupId
        val pattern = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val newCalendar = Calendar.getInstance(Locale.getDefault())
        val neededDate = simpleDateFormat.format(newCalendar.time)
        val expenseId = UUID.randomUUID().toString()

        val expenseEntity = Expense(expenseId = expenseId, name = name, amount = amount, user = user, groupId = groupId, date = neededDate, uploaded = true)
        viewModelScope.launch(Dispatchers.Main) {
            createExpense.invoke(expenseEntity).observeForever { _addExpenseResult.postValue(it) }
        }
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.Main) {
            downloadUser.invoke().observeForever { _user.postValue(it) }
        }
    }

    private fun getGroups() {
        viewModelScope.launch(Dispatchers.Main) {
            getGroupByGroupId.invoke().observeForever { _group.postValue(it) }
        }
    }

    fun getUsersFromGroup(group: Group) {
        val myUid = FirebaseAuth.getInstance().uid
        val filteredUserArrayList = arrayListOf<User>()
        viewModelScope.launch(Dispatchers.IO) {
            val usersFromGroup = group.users
            usersFromGroup.forEach {
                if (it.uid != myUid) {
                    filteredUserArrayList.add(it)
                }
            }
            _users.postValue(filteredUserArrayList)
        }
    }
}
