package com.example.expensare.ui.addexpense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensare.data.*
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


    init {
        getUserInfo()
        getGroupByGroupId()
    }

    fun createDebt(amount: Int, fromUser: User, toUser: User) {
        val storage = Storage(getApplication())
        val groupId = storage.groupId
        val uid = UUID.randomUUID().toString()
        val uid2 = UUID.randomUUID().toString()
        viewModelScope.launch(Dispatchers.IO) {

            val toReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("group_debts/$groupId/${fromUser.uid}/owe/${toUser.uid}")
            val fromReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("group_debts/$groupId/${toUser.uid}/lent/${fromUser.uid}")
            val toReferenceCheck = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("group_debts/$groupId/${fromUser.uid}/owe")
            val fromReferenceCheck = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("group_debts/$groupId/${toUser.uid}/lent")

            toReferenceCheck.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val oweChild = snapshot.child("/${toUser.uid}/amount/").getValue(Int::class.java)
                        if (oweChild == null) {
                            val toDebt = FirebaseDebt(uid, user = toUser, amount = amount)
                            toReference.setValue(toDebt)
                        } else {
                            toReference.child("/amount/").setValue(oweChild + amount)
                        }
                    } else {
                        val toDebt = FirebaseDebt(uid, user = toUser, amount = amount)
                        toReference.setValue(toDebt)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

                fromReferenceCheck.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val lentChild = snapshot.child("/${fromUser.uid}/amount/").getValue(Int::class.java)
                        if (lentChild == null) {
                            val fromDebt = FirebaseDebt(uid2, user = toUser, amount = amount)
                            fromReference.setValue(fromDebt).addOnSuccessListener {
                                _addDebtResult.postValue(AddDebtResult.Success)
                            }
                                .addOnFailureListener {
                                    _addDebtResult.postValue(AddDebtResult.Error(it))
                                }
                        } else {
                            val insertAmount = lentChild + amount
                            fromReference.child("/amount/").setValue(insertAmount)
                        }

                    } else {
                        val fromDebt = FirebaseDebt(uid2, user = toUser, amount = amount)
                        fromReference.setValue(fromDebt).addOnSuccessListener {
                            _addDebtResult.postValue(AddDebtResult.Success)
                        }
                            .addOnFailureListener {
                                _addDebtResult.postValue(AddDebtResult.Error(it))
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
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
        userIdArrayList.forEach { currentUserId ->
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
                                        if (userInfo.uid == currentUserId) {
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