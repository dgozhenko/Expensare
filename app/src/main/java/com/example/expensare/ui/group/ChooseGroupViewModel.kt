package com.example.expensare.ui.group

import android.app.Application
import androidx.lifecycle.*
import com.example.expensare.data.Group
import com.example.expensare.ui.storage.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChooseGroupViewModel(private val getApplication: Application): AndroidViewModel(getApplication) {

    private val _groups = MutableLiveData<ArrayList<Group>>()
    val groups: LiveData<ArrayList<Group>> get() = _groups

    fun saveGroupID(groupId: String) {
        val storage = Storage(getApplication.baseContext)
        storage.groupId = groupId
    }

    fun listenForGroups() {
        val userID = FirebaseAuth.getInstance().uid
        val allGroups = arrayListOf<Group>()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("groups/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val group = it.getValue(Group::class.java)
                            group?.users?.forEach{ user ->
                                if (user == userID) {
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

}