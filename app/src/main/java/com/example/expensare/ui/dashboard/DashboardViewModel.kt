package com.example.expensare.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensare.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

  private val _user = MutableLiveData<User>()
  val user: LiveData<User>
    get() = _user

  init {
    getUserInfo()
  }

  private fun getUserInfo() {
    val userId = FirebaseAuth.getInstance().uid
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
                  val userInfo = it.getValue(User::class.java)
                  if (userInfo != null) {
                    if (userInfo.uid == userId) {
                      _user.postValue(userInfo)
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
