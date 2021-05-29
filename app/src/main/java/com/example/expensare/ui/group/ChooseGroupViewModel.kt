package com.example.expensare.ui.group

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.expensare.data.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChooseGroupViewModel(application: Application): AndroidViewModel(application) {

    private val _groups = MutableLiveData<ArrayList<Group>>()
    val groups: LiveData<ArrayList<Group>> get() = _groups

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
                            if (group != null) {
                                group.users.forEach{ user ->
                                    if (user == userID) {
                                        allGroups.add(group)
                                        _groups.postValue(allGroups)
                                        Log.d("NOWCHECK", allGroups.toString())
                                    } else {
                                        Toast.makeText(getApplication<Application>().baseContext, "No USer", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(getApplication<Application>().baseContext, "No group", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(getApplication<Application>().baseContext, "No snapshot", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

}