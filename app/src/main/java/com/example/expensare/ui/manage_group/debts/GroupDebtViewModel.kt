package com.example.expensare.ui.manage_group.debts

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
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

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

    private val _detailedUserDebt = MutableLiveData<ArrayList<UserDebt>>()
    val detailedUserDebt: LiveData<ArrayList<UserDebt>> get() = _detailedUserDebt

    init {
        getGroupByGroupId()
        getUserInfo()
    }

    fun getDetailedDebts(user: User) {
        val storage = Storage(getApplication())
        val groupId = storage.groupId
        val userDebtArrayList = arrayListOf<UserDebt>()
                val toReference =
                    FirebaseDatabase.getInstance(
                        "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                    )
                        .getReference("group_debts/$groupId/${user.uid}/lent")
                val fromReference =
                    FirebaseDatabase.getInstance(
                        "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                    )
                        .getReference("group_debts/$groupId/${user.uid}/owe")
                toReference.addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                snapshot.children.forEach {
                                    val amount = it.child("/amount/").getValue(Int::class.java)
                                    val userFromData = it.child("/user/").getValue(User::class.java)
                                    if (amount != null && userFromData != null) {
                                        userDebtArrayList.add(UserDebt(amount, userFromData, false))
                                        _detailedUserDebt.postValue(userDebtArrayList)
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

    fun getDebts(user: ArrayList<User>, debtToMe: Boolean) {
        val storage = Storage(getApplication())
        val groupId = storage.groupId
        var fullAmount = 0
        var oppositeAmount = 0
        var newAmount = 0
        val userDebtArrayList = arrayListOf<UserDebt>()
        val oppositeDebtUserArray = arrayListOf<UserDebt>()
        val tempToUserArray = arrayListOf<User>()
        val equalizedDebtArray = arrayListOf<UserDebt>()
        user.forEach { tempToUserArray.add(it) }
        user.forEach { groupUser ->
            if (debtToMe) {
                val toReference =
                    FirebaseDatabase.getInstance(
                            "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                        )
                        .getReference("group_debts/$groupId/${groupUser.uid}/lent")
                val fromReference =
                    FirebaseDatabase.getInstance(
                            "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                        )
                        .getReference("group_debts/$groupId/${groupUser.uid}/owe")
                toReference.addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                snapshot.children.forEach {
                                    val amount = it.child("/amount/").getValue(Int::class.java)
                                    val userFromData = it.child("/user/").getValue(User::class.java)
                                    if (amount != null && userFromData != null) {
                                        fullAmount += amount
                                    }
                                }
                            }
                            if (fullAmount != 0) {
                                userDebtArrayList.add(UserDebt(fullAmount, groupUser, false))
                                fullAmount = 0
                                _userDebt.postValue(userDebtArrayList)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    }
                )
            } else {
                val reference =
                    FirebaseDatabase.getInstance(
                            "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                        )
                        .getReference("group_debts/$groupId/${groupUser.uid}/owe")
                val oppositeReference =
                    FirebaseDatabase.getInstance(
                            "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                        )
                        .getReference("group_debts/$groupId/${groupUser.uid}/lent")

                oppositeReference.addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                tempToUserArray.forEach { tempUser ->
                                    val amount =
                                        snapshot.child("/${tempUser.uid}/amount/").getValue(Int::class.java)
                                    val userFromData =
                                        snapshot.child("${tempUser.uid}/user/").getValue(User::class.java)
                                    if (amount != null && userFromData != null) {
                                        oppositeAmount += amount
                                        if (oppositeAmount != 0) {
                                            oppositeDebtUserArray.add(UserDebt(oppositeAmount, groupUser, false))
                                            oppositeAmount = 0
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

                reference.addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                snapshot.children.forEach {

                                        val amount = it.child("/amount/").getValue(Int::class.java)
                                        val userFromData =
                                            it.child("/user/").getValue(User::class.java)
                                        if (amount != null &&
                                                userFromData != null
                                        ) {
                                            fullAmount += amount
                                        }
                                }
                                userDebtArrayList.add(UserDebt(fullAmount, groupUser, false))
                                _userDebt.postValue(userDebtArrayList)
                                fullAmount = 0
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    }
                )
            }
        }
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
                                snapshot.children.forEach { user ->
                                    val userInfo = user.getValue(User::class.java)
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
