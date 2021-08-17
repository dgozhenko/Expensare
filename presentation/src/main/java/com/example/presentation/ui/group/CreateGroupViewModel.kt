package com.example.presentation.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

sealed class CreateGroupResult {
    object Success : CreateGroupResult()
    data class Error(val exception: Exception) : CreateGroupResult()
}

@HiltViewModel
class CreateGroupViewModel @Inject constructor() : ViewModel() {

    private val _createGroupResult = MutableLiveData<CreateGroupResult>()
    val createGroupResult: LiveData<CreateGroupResult>
        get() = _createGroupResult

    // TODO: 17.08.2021 Repository
    fun createGroup(groupName: String, groupType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val users = arrayListOf<String>()
            val groupId = UUID.randomUUID().toString()
            users.add(uid)
            val reference =
                FirebaseDatabase.getInstance(
                        "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
                    )
                    .getReference("groups/")
                    .push()
            val group = Group(groupID = groupId, groupName = groupName, groupType = groupType, users = users)
            reference
                .setValue(group)
                .addOnCompleteListener { _createGroupResult.postValue(CreateGroupResult.Success) }
                .addOnFailureListener { _createGroupResult.postValue(CreateGroupResult.Error(it)) }
        }
    }
}
