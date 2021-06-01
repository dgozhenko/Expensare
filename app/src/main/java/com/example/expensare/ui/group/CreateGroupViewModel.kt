package com.example.expensare.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensare.data.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class CreateGroupResult {
    object Success : CreateGroupResult()
    data class Error(val exception: Exception) : CreateGroupResult()
}

class CreateGroupViewModel : ViewModel() {

    private val _createGroupResult = MutableLiveData<CreateGroupResult>()
    val createGroupResult: LiveData<CreateGroupResult>
        get() = _createGroupResult

    fun createGroup(groupName: String, groupType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val users = arrayListOf<String>()
            users.add(uid)
            val reference =
                FirebaseDatabase.getInstance(
                        "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                    )
                    .getReference("groups/")
                    .push()
            val group = Group(groupName = groupName, groupType = groupType, users = users)
            reference
                .setValue(group)
                .addOnCompleteListener { _createGroupResult.postValue(CreateGroupResult.Success) }
                .addOnFailureListener { _createGroupResult.postValue(CreateGroupResult.Error(it)) }
        }
    }
}
