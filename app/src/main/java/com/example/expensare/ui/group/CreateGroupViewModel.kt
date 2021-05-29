package com.example.expensare.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensare.data.Group
import com.example.expensare.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class CreateGroupViewModel: ViewModel() {

    private val _isComplete = MutableLiveData<Boolean>()
    val isComplete: LiveData<Boolean> get() = _isComplete

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> get() = _error

    fun createGroup(groupName: String, groupType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userID = FirebaseAuth.getInstance().uid
            val users = arrayListOf<String>()
            users.add(userID!!)
            val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("groups/").push()
            val group = Group(groupName = groupName, groupType = groupType, users = users)
            reference.setValue(group).addOnCompleteListener {
                _isComplete.postValue(true)
                _error.postValue(null)
            }
                .addOnFailureListener {
                    _error.postValue(it)
                }
        }
    }

    fun completed() {
        _isComplete.postValue(null)
    }

    fun errorDelivered() {
        _error.postValue(null)
    }
}