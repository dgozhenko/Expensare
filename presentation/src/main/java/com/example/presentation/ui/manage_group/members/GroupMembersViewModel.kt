package com.example.presentation.ui.manage_group.members

import androidx.lifecycle.*
import com.example.domain.models.Group
import com.example.domain.models.User
import com.example.presentation.ui.storage.Storage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupMembersViewModel @Inject constructor(private val storage: Storage): ViewModel() {

    private val _user = MutableLiveData<ArrayList<User>>()
    val user: LiveData<ArrayList<User>>
        get() = _user

    private val _userByEmail = MutableLiveData<User>()
    val userByEmail: LiveData<User> get() = _userByEmail

    private val _group = MutableLiveData<Group>()
    val group: LiveData<Group> get() = _group

    init {
        getGroupByGroupId()
    }

    private fun getGroupByGroupId() {
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

    fun addUserToGroup(user: User) {
        val groupId = storage.groupId
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/groups/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent((object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {

                            val group = it.getValue(Group::class.java)
                            if (group != null) {
                                if (group.groupID == groupId) {
                                    val groupKey = it.key
                                    val usersIdArray = group.users
                                    createUserInGroup(user, groupKey, usersIdArray)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }))
        }
    }

    private fun createUserInGroup(user: User, groupKey: String?, usersIdArray: ArrayList<String>) {
        usersIdArray.add(user.uid)
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/groups/")
        reference.child(groupKey!!).child("users").setValue(usersIdArray)
    }

    fun getUserByEmail(email: String) {
        val users = arrayListOf<String>()
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
                                    if (userInfo.email == email) {
                                        users.add(userInfo.email)
                                        _userByEmail.postValue(userInfo)
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }

     fun getUsersFromGroup(group: Group) {
        val userIdArrayList = arrayListOf<String>()
        val userArrayList = arrayListOf<User>()
        group.users.forEach {
            userIdArrayList.add(it)
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
                                snapshot.children.forEach { userChild ->
                                    val userInfo = userChild.getValue(User::class.java)
                                    if (userInfo != null) {
                                        if (userInfo.uid == userId) {
                                            userArrayList.add(userInfo)
                                        }
                                    }
                                }
                                _user.postValue(userArrayList)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
            }
        }

    }

}