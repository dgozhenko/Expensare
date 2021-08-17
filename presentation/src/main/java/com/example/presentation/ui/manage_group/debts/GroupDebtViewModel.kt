package com.example.presentation.ui.manage_group.debts

import androidx.lifecycle.*
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
import kotlin.collections.ArrayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDebtViewModel @Inject constructor(private val storage: Storage) : ViewModel() {

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
    val detailedUserDebt: LiveData<ArrayList<UserDebt>>
        get() = _detailedUserDebt

    init {
        getGroupByGroupId()
        getUserInfo()
    }

    fun getDetailedDebts(user: User, debtToMe: Boolean) {
        val groupId = storage.groupId
        val userDebtArrayList = arrayListOf<UserDebt>()
        if (debtToMe) {
            val referenceCheck = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("group_debts/$groupId/")
            referenceCheck.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                val userDebt = it.getValue(UserDebt::class.java)!!
                                if (userDebt.firstUser == user && userDebt.firstUserAmount > 0) {
                                    // we need secondUser
                                    userDebtArrayList.add(UserDebt(userDebt.secondUser, userDebt.secondUser, userDebt.secondUserAmount, userDebt.secondUserAmount, false))
                                } else if (userDebt.secondUser == user && userDebt.secondUserAmount > 0) {
                                    // we need firsUser
                                    userDebtArrayList.add(UserDebt(userDebt.firstUser, userDebt.firstUser, userDebt.firstUserAmount, userDebt.firstUserAmount, false))
                                }
                            }
                            _detailedUserDebt.postValue(userDebtArrayList)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )
        } else {
            val referenceCheck = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("group_debts/$groupId/")
            referenceCheck.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                val userDebt = it.getValue(UserDebt::class.java)!!
                                if (userDebt.firstUser == user && userDebt.firstUserAmount < 0) {
                                    // we need secondUser
                                    userDebtArrayList.add(UserDebt(userDebt.secondUser, userDebt.secondUser, userDebt.secondUserAmount, userDebt.secondUserAmount, false))
                                } else if (userDebt.secondUser == user && userDebt.secondUserAmount < 0) {
                                    // we need firstUser
                                    userDebtArrayList.add(UserDebt(userDebt.firstUser, userDebt.firstUser, userDebt.firstUserAmount, userDebt.firstUserAmount, false))
                                }
                            }
                            _detailedUserDebt.postValue(userDebtArrayList)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )
        }
    }

    fun getDebts(user: ArrayList<User>, debtToMe: Boolean) {
        val groupId = storage.groupId
        var fullAmount = 0
        val userDebtArrayList = arrayListOf<UserDebt>()
        user.forEach { groupUser ->
            if (debtToMe) {
                val referenceCheck = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("group_debts/$groupId/")
                referenceCheck.addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                snapshot.children.forEach {
                                    val userDebt = it.getValue(UserDebt::class.java)!!
                                    if (userDebt.firstUser == groupUser && userDebt.firstUserAmount > 0) {
                                        fullAmount += userDebt.firstUserAmount
                                    } else if (userDebt.secondUser == groupUser && userDebt.secondUserAmount > 0) {
                                        fullAmount += userDebt.secondUserAmount
                                    }
                                }
                                if (fullAmount != 0) {
                                    userDebtArrayList.add(UserDebt(groupUser, groupUser, fullAmount, fullAmount, false))
                                    fullAmount = 0
                                    _userDebt.postValue(userDebtArrayList)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    }
                )
            } else {
                val referenceCheck = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("group_debts/$groupId/")
                referenceCheck.addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                snapshot.children.forEach {
                                    val userDebt = it.getValue(UserDebt::class.java)!!
                                    if (userDebt.firstUser == groupUser && userDebt.firstUserAmount < 0) {
                                        fullAmount += userDebt.firstUserAmount
                                    } else if (userDebt.secondUser == groupUser && userDebt.secondUserAmount < 0) {
                                        fullAmount += userDebt.secondUserAmount
                                    }
                                }
                                if (fullAmount != 0) {
                                    userDebtArrayList.add(UserDebt(groupUser, groupUser, fullAmount * -1, fullAmount * -1, false))
                                    fullAmount = 0
                                    _userDebt.postValue(userDebtArrayList)
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
    }

    private fun getGroupByGroupId() {
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
