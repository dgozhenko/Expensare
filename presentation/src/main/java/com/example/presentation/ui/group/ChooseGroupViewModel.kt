package com.example.presentation.ui.group

import androidx.lifecycle.*
import com.example.data.interactors.user.DownloadUser
import com.example.domain.models.Group
import com.example.data.storage.Storage
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseGroupViewModel @Inject constructor(private val storage: Storage, private val downloadUser: DownloadUser): ViewModel() {

    private val _groups = MutableLiveData<ArrayList<Group>>()
    val groups: LiveData<ArrayList<Group>> get() = _groups

    private val _user = MutableLiveData<Response<UserEntity>>()
    val user: LiveData<Response<UserEntity>>
        get() = _user

    fun saveGroupID(groupId: String) {
        storage.groupId = groupId
    }

    init {
        getUserInfo()
    }

    // TODO: 17.08.2021 Repository
    fun listenForGroups(userEntity: UserEntity) {
        val allGroups = arrayListOf<Group>()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("groups/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val group = it.getValue(Group::class.java)
                            group?.users?.forEach{ user ->
                                if (user == userEntity) {
                                    allGroups.add(group)
                                    _groups.postValue(allGroups)
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

    private fun getUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            downloadUser.invoke().observeForever {
             _user.postValue(it)
            }
        }
    }

}