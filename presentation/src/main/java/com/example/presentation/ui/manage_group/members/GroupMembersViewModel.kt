package com.example.presentation.ui.manage_group.members

import androidx.lifecycle.*
import com.example.domain.models.Group
import com.example.domain.models.User
import com.example.data.storage.Storage
import com.example.domain.database.entities.UserEntity
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

    private val _userByEmail = MutableLiveData<UserEntity>()
    val userByEmail: LiveData<UserEntity> get() = _userByEmail

    private val _group = MutableLiveData<Group>()
    val group: LiveData<Group> get() = _group

    init {
        getGroupByGroupId()
    }
    // TODO: 17.08.2021 Repository
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

    // TODO: 17.08.2021 Repository
    fun addUserToGroup(user: UserEntity) {
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

    // TODO: 17.08.2021 Repository
    private fun createUserInGroup(user: UserEntity, groupKey: String?, usersIdArray: ArrayList<UserEntity>) {
        usersIdArray.add(user)
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/groups/")
        reference.child(groupKey!!).child("users").setValue(usersIdArray)
    }

    // TODO: 17.08.2021 Repository
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
                                val userInfo = it.getValue(UserEntity::class.java)
                                if (userInfo != null) {
                                    if (userInfo.userEmail == email) {
                                        users.add(userInfo.userEmail)
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

    // TODO: 17.08.2021 Repository
     fun getUsersFromGroup(group: Group) {

    }

}