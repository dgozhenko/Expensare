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
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
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
constructor(private val storage: Storage, database: ExpensareDatabase,
            private val downloadUser: DownloadUser,
            private val createExpense: CreateExpense,
            private val createDebt: CreateDebt,
            private val getAllUsersFromGroup: GetAllUsersFromGroup,
            private val getGroupByGroupId: GetGroupByGroupId) : ViewModel() {

    private val _user = MutableLiveData<Response<UserEntity>>()
    val user: LiveData<Response<UserEntity>>
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

    private val _users = MutableLiveData<ArrayList<UserEntity>>()
    val users: LiveData<ArrayList<UserEntity>>
        get() = _users

    init {
        getUser()
        getGroups()
    }
    // TODO: 17.08.2021 Repository
    fun createDebt(amount: Int, fromUser: UserEntity, toUser: UserEntity) {
        val debt = UserDebt(toUser, fromUser, amount, amount, false)
        viewModelScope.launch(Dispatchers.Main) {
            createDebt.invoke(debt).observeForever {
                _addDebtResult.postValue(it)
            }
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
        viewModelScope.launch(Dispatchers.Main) {
            createExpense.invoke(expenseEntity).observeForever {
                _addExpenseResult.postValue(it)
            }
        }

    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.Main) {
            downloadUser.invoke().observeForever {
                _user.postValue(it)
            }
        }
    }

    private fun getGroups() {
        viewModelScope.launch(Dispatchers.Main) {
            getGroupByGroupId.invoke().observeForever {
                _group.postValue(it)
            }
        }
    }
    // TODO: 17.08.2021 Repository
    // SecondUser ??????
    fun getUsersFromGroup(group: Group) {
        val myUid = FirebaseAuth.getInstance().uid
        val filteredUserArrayList = arrayListOf<UserEntity>()
        viewModelScope.launch(Dispatchers.IO) {
            val usersFromGroup = group.users
            usersFromGroup.forEach {
                if (it.userUidId != myUid) {
                    filteredUserArrayList.add(it)
                }
            }
            _users.postValue(filteredUserArrayList)
        }
    }
}
