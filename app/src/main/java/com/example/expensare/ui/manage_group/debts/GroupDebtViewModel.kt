package com.example.expensare.ui.manage_group.debts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensare.data.Debt
import com.example.expensare.data.Group
import com.example.expensare.data.User
import com.example.expensare.data.UserDebt
import com.example.expensare.ui.storage.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupDebtViewModel(getApplication: Application) : AndroidViewModel(getApplication) {

    private val _group = MutableLiveData<Group>()
    val group: LiveData<Group>
        get() = _group

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _users = MutableLiveData<ArrayList<User>>()
    val users: LiveData<ArrayList<User>>
        get() = _users

    private val _userDebt = MutableLiveData<ArrayList<UserDebt>>()
    val userDebt: LiveData<ArrayList<UserDebt>>
        get() = _userDebt

    init {
        getGroupByGroupId()
        getUserInfo()
    }

    fun getDebts(user: ArrayList<User>, debtToMe: Boolean) {
        val storage = Storage(getApplication())
        val groupId = storage.groupId
        var fullAmount = 0
        var fromUser = User("", "", "", null)
        var toUser = User("", "", "", null)
        val userDebtArrayList = arrayListOf<UserDebt>()

        val reference =
            FirebaseDatabase.getInstance(
                    "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                )
                .getReference("group_debts/$groupId")
        reference.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        user.forEach { getUser ->
                            snapshot.children.forEach { debtChildren ->
                                val debt = debtChildren.getValue(Debt::class.java)!!

                                if (debtToMe) {
                                    if (debt.toUser == getUser) {
                                        fullAmount += debt.amount
                                        fromUser = debt.fromUser
                                        toUser = debt.toUser
                                    }
                                } else {
                                    if (getUser == debt.fromUser) {
                                        fullAmount += debt.amount
                                        fromUser = debt.fromUser
                                        toUser = debt.toUser
                                    }
                                }
                            }
                            if (fullAmount != 0) {
                                if (debtToMe) {
                                    userDebtArrayList.add(UserDebt(fullAmount, fromUser, toUser))
                                    fullAmount = 0
                                } else {
                                    userDebtArrayList.add(UserDebt(fullAmount, fromUser, fromUser))
                                    fullAmount = 0
                                }
                                _userDebt.postValue(userDebtArrayList)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
        )
    }

    private fun getGroupByGroupId() {
        val storage = Storage(getApplication())
        val groupId = storage.groupId
        val reference =
            FirebaseDatabase.getInstance(
                    "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                )
                .getReference("/groups/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(
                object : ValueEventListener {
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
                }
            )
        }
    }

    private fun getUserInfo() {
        val userId = FirebaseAuth.getInstance().uid
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
                }
            )
        }
    }

    fun getUsersFromGroup(group: Group) {
        val userIdArrayList = arrayListOf<String>()
        val userArrayList = arrayListOf<User>()
        group.users.forEach { userIdArrayList.add(it) }
        userIdArrayList.forEach {
            val userId = it
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
                                        if (userInfo.uid == userId) {
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
