package com.example.presentation.ui.dashboard.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Group
import com.example.domain.models.ListItem
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
class ListViewModel @Inject constructor(private val storage: Storage): ViewModel() {

    private val _group = MutableLiveData<Group>()
    val group: LiveData<Group> get() = _group

    private val _groceryList = MutableLiveData<ArrayList<ListItem>>()
    val groceryList: LiveData<ArrayList<ListItem>> get() = _groceryList

    private val _refreshedGroceryList = MutableLiveData<ArrayList<ListItem>>()
    val refreshedGroceryList: LiveData<ArrayList<ListItem>> get() = _refreshedGroceryList

    init {
        getGroupByGroupId()
        getGroupGroceryList()
    }

    fun refreshGroceryList() {
        val groupId = storage.groupId
        val groceryListArray = arrayListOf<ListItem>()
        val reference = FirebaseDatabase.getInstance(
            "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("/grocery_list/$groupId/")
        reference.keepSynced(true)
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val groceryListItem = it.getValue(ListItem::class.java)
                            if (groceryListItem != null) {
                                groceryListArray.add(groceryListItem)
                            }
                        }
                        _refreshedGroceryList.postValue(groceryListArray)
                    } else {
                        _refreshedGroceryList.postValue(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    fun deleteGroceryItem(item: ListItem) {
        val groupId = storage.groupId
        val reference = FirebaseDatabase.getInstance(
            "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("/grocery_list/$groupId/")
        val deleteReference = FirebaseDatabase.getInstance(
            "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("/grocery_list/$groupId/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                        val listItem = it.getValue(ListItem::class.java)
                            if (item == listItem) {
                                val key = it.key
                                deleteReference.child("${key!!}/").removeValue()
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

    private fun getGroupGroceryList() {
        val groupId = storage.groupId
        val groceryListArray = arrayListOf<ListItem>()
        val reference = FirebaseDatabase.getInstance(
            "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("/grocery_list/$groupId/")
        reference.keepSynced(true)
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val groceryListItem = it.getValue(ListItem::class.java)
                            if (groceryListItem != null) {
                                groceryListArray.add(groceryListItem)
                            }
                        }
                        _groceryList.postValue(groceryListArray)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun getGroupByGroupId() {
        val groupId = storage.groupId
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/groups/")
        reference.keepSynced(true)
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
}

