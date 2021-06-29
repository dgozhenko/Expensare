package com.example.expensare.ui.dashboard

import android.app.Application
import androidx.lifecycle.*
import com.example.expensare.data.models.Expense
import com.example.expensare.data.models.Group
import com.example.expensare.data.models.User
import com.example.expensare.ui.storage.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardViewModel @Inject constructor(private val storage: Storage) : ViewModel() {

  private val _user = MutableLiveData<User>()
  val user: LiveData<User>
    get() = _user

  private val _group = MutableLiveData<Group>()
  val group: LiveData<Group> get() = _group

  private val _expenses = MutableLiveData<ArrayList<Expense>>()
  val expense: LiveData<ArrayList<Expense>> get() = _expenses

  init {
    getUserInfo()
    getGroupByGroupId()
    getGroupExpenses()
  }

  private fun getGroupExpenses() {
    val groupId = storage.groupId
    val expensesArrayList = arrayListOf<Expense>()
    val reference = FirebaseDatabase.getInstance(
      "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
      .getReference("/expenses/$groupId/")
    reference.keepSynced(true)
    viewModelScope.launch(Dispatchers.IO) {
      reference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          if (snapshot.exists()) {
            snapshot.children.forEach {
              val expense = it.getValue(Expense::class.java)
              if (expense != null) {
                expensesArrayList.add(expense)
              }
            }
            _expenses.postValue(expensesArrayList)
          }
        }

        override fun onCancelled(error: DatabaseError) {
          TODO("Not yet implemented")
        }

      })
    }
  }

  private fun getUserInfo() {
    val userId = FirebaseAuth.getInstance().uid
    val reference =
        FirebaseDatabase.getInstance(
                "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("/users/")
    reference.keepSynced(true)
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
          })
    }
  }

  private fun getGroupByGroupId() {
    val groupId = storage.groupId
    val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/groups/")
    reference.keepSynced(true)
    viewModelScope.launch(Dispatchers.IO) {
      reference.addListenerForSingleValueEvent(object : ValueEventListener{
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
