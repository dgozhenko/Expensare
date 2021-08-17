package com.example.presentation.ui.dashboard.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Group
import com.example.domain.models.ListItem
import com.example.domain.models.User
import com.example.data.storage.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

sealed class AddToListResult {
    object Success : AddToListResult()
    data class Error(val exception: Exception) : AddToListResult()
}

@HiltViewModel
class AddToListViewModel @Inject constructor(private val storage: Storage): ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _addToListResult = MutableLiveData<AddToListResult>()
    val addToListResult: LiveData<AddToListResult>
        get() = _addToListResult

    private val _group = MutableLiveData<Group>()
    val group: LiveData<Group>
        get() = _group

    private val _users = MutableLiveData<ArrayList<User>>()
    val users: LiveData<ArrayList<User>>
        get() = _users

    init {
        getUserInfo()
        getGroupByGroupId()
    }

    fun createListItem(category: String, name: String, user: User, quantity: Int, store: String) {
        val groupId = storage.groupId
        viewModelScope.launch(Dispatchers.IO) {
            val reference =
                FirebaseDatabase.getInstance(
                    "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                )
                    .getReference("grocery_list/$groupId")
                    .push()
            reference.keepSynced(true)
            val listItem = ListItem(store = store, quantity = quantity, name = name, type = category, false, user = user)

            val connectedRef = FirebaseDatabase.getInstance(
                "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
            ).getReference(".info/connected")
            connectedRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connected = snapshot.getValue(Boolean::class.java) ?: false
                    if (connected) {
                        reference
                            .setValue(listItem)
                            .addOnSuccessListener { _addToListResult.postValue(AddToListResult.Success)}
                            .addOnFailureListener { _addToListResult.postValue(AddToListResult.Error(it)) }
                    } else {
                        Log.d("TAG", "not connected")
                        reference
                            .setValue(listItem)
                        _addToListResult.postValue(AddToListResult.Success)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("TAG", "Listener was cancelled")
                }
            })
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