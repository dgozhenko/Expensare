package com.example.expensare.ui.addexpense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensare.data.Debt
import com.example.expensare.data.Expense
import com.example.expensare.data.Group
import com.example.expensare.data.User
import com.example.expensare.ui.storage.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

sealed class AddExpenseResult {
    object Success : AddExpenseResult()
    data class Error(val exception: Exception) : AddExpenseResult()
}

sealed class AddDebtResult {
    object Success : AddDebtResult()
    data class Error(val exception: Exception) : AddDebtResult()
}

class AddExpenseViewModel(getApplication: Application): AndroidViewModel(getApplication) {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _addExpenseResult = MutableLiveData<AddExpenseResult>()
    val addExpenseResult: LiveData<AddExpenseResult>
        get() = _addExpenseResult

    private val _addDebtResult = MutableLiveData<AddDebtResult>()
    val addDebtResult: LiveData<AddDebtResult>
        get() = _addDebtResult

    private val _group = MutableLiveData<Group>()
    val group: LiveData<Group> get() = _group

    private val _users = MutableLiveData<ArrayList<User>>()
    val users: LiveData<ArrayList<User>>
        get() = _users

    private val _test = MutableLiveData<Boolean>()
    val test: LiveData<Boolean>
    get() = _test

    init {
        getUserInfo()
        getGroupByGroupId()
    }

    fun setupTest(equally: Boolean) {
        _test.postValue(equally)
    }

    fun createDebt(amount: Int, from: String) {
        val userId = FirebaseAuth.getInstance().uid
        val storage = Storage(getApplication())
        val groupId = storage.groupId

        viewModelScope.launch(Dispatchers.IO) {
            val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("group_debts/$groupId/$userId").push()
            val debt = Debt(to = userId!!, from = from, amount = amount)
            reference.setValue(debt)
                .addOnSuccessListener {
                    _addDebtResult.postValue(AddDebtResult.Success)
                }
                .addOnFailureListener {
                    _addDebtResult.postValue(AddDebtResult.Error(it))
                }
        }
    }

    fun createExpense(name: String, amount: Int, user: User) {
        val storage = Storage(getApplication())
        val groupId = storage.groupId
        // User + Date
        val pattern = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val newCalendar = Calendar.getInstance(Locale.getDefault())
        val neededDate = simpleDateFormat.format(newCalendar.time)

        viewModelScope.launch(Dispatchers.IO) {
            val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("expenses/$groupId").push()
            val expense = Expense(name = name, amount = amount, user = user, groupId = groupId, date = neededDate)
            reference.setValue(expense).addOnSuccessListener { _addExpenseResult.postValue(AddExpenseResult.Success) }
                .addOnFailureListener { _addExpenseResult.postValue(AddExpenseResult.Error(it)) }

        }
    }

    private fun getGroupByGroupId() {
        val storage = Storage(getApplication())
        val groupId = storage.groupId
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/groups/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val groupInfo = it.getValue(Group::class.java)
                            if (groupInfo != null) {
                                if (groupInfo.groupID == groupId) {
                                    _group.postValue(groupInfo)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun getUserInfo() {
        val userId = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance(
                "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/users/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var userIsExist = false
                            snapshot.children.forEach {
                                val userInfo = it.getValue(User::class.java)
                                if (userInfo != null) {
                                    if (userInfo.uid == userId) {
                                        userIsExist = true
                                        _user.postValue(userInfo)
                                    }
                                }
                                if (!userIsExist) {
                                    _user.postValue(null)
                                }
                            }
                        } else {
                            _user.postValue(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
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
        userIdArrayList.forEach {
            val userId = it
            val reference =
                FirebaseDatabase.getInstance(
                    "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("/users/")
            viewModelScope.launch(Dispatchers.IO) {
                reference.addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                snapshot.children.forEach {
                                    val userInfo = it.getValue(User::class.java)
                                    if (userInfo != null) {
                                        if (userInfo.uid == userId) {
                                            userArrayList.add(userInfo)
                                        }
                                    }
                                }
                                _users.postValue(userArrayList)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
            }
        }

    }
}