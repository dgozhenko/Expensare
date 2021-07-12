package com.example.expensare.ui.mydebts.create_debt

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensare.data.models.Group
import com.example.expensare.data.models.ManualDebt
import com.example.expensare.data.models.User
import com.example.expensare.ui.addexpense.AddExpenseResult
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

sealed class CreateDebtResult {
    object Success : CreateDebtResult()
    data class Error(val exception: Exception) : CreateDebtResult()
}

class CreateDebtViewModel (private val getApplication: Application) : AndroidViewModel(getApplication) {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _group = MutableLiveData<Group>()
    val group: LiveData<Group> get() = _group

    private val _users = MutableLiveData<ArrayList<User>>()
    val users: LiveData<ArrayList<User>>
        get() = _users

    private val _createDebtResult = MutableLiveData<CreateDebtResult>()
    val addExpenseResult: LiveData<CreateDebtResult>
        get() = _createDebtResult

    init {
        getUserInfo()
        getGroupByGroupId()
    }

    private fun getUserInfo() {
        val userId = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
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
                                        Log.d("userInfo", user.value.toString())
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

    private fun getGroupByGroupId() {
        val storage = Storage(getApplication.baseContext)
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

    fun getUsersFromGroup(group: Group) {
        val userId = FirebaseAuth.getInstance().uid
        val userIdArrayList = arrayListOf<String>()
        val userArrayList = arrayListOf<User>()
        group.users.forEach {
            if (userId != it) {
                Log.d("userId", userId.toString())
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
                                            Log.d("userArraylist", userInfo.toString())
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

    fun createDebt(debtFor: String, amount: Int, fromUser: User, toUser: User) {
        val pattern = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val newCalendar = Calendar.getInstance(Locale.getDefault())
        val neededDate = simpleDateFormat.format(newCalendar.time)
        val debtId = UUID.randomUUID().toString()

        viewModelScope.launch(Dispatchers.IO) {
            val referenceCheck = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("manual_debts/${fromUser.uid}/")
            val referenceAddLent = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("manual_debts/${fromUser.uid}/lent/$debtId")
            val referenceAddOwe = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("manual_debts/${toUser.uid}/owe/$debtId")
            referenceCheck.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    referenceAddLent.setValue(ManualDebt(debtId,toUser, fromUser, amount, debtFor, neededDate))
                    referenceAddOwe.setValue(ManualDebt(debtId, toUser, fromUser, amount, debtFor, neededDate))
                    _createDebtResult.postValue(CreateDebtResult.Success)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}