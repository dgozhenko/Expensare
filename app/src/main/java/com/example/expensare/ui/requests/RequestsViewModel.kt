package com.example.expensare.ui.requests

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensare.data.models.Request
import com.example.expensare.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RequestsViewModel(private val getApplication: Application) : AndroidViewModel(getApplication) {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _toMeRequests = MutableLiveData<ArrayList<Request>>()
    val toMeRequests: LiveData<ArrayList<Request>>
        get() = _toMeRequests

    private val _pendingRequests = MutableLiveData<ArrayList<Request>>()
    val pendingRequests: LiveData<ArrayList<Request>>
        get() = _pendingRequests

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        val userId = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
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
                })
        }
    }

    fun getPendingRequests(){
        val userId = FirebaseAuth.getInstance().uid
        val requestsArrayList = arrayListOf<Request>()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/requests/$userId/requested/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val request = it.getValue(Request::class.java)!!
                            requestsArrayList.add(request)
                        }
                        _toMeRequests.postValue(requestsArrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}