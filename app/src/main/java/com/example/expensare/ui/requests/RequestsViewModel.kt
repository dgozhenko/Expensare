package com.example.expensare.ui.requests

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensare.data.models.ManualDebt
import com.example.expensare.data.models.Request
import com.example.expensare.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

sealed class acceptHandlerResult {
    object Success : acceptHandlerResult()
    data class Error(val exception: Exception) : acceptHandlerResult()
}

class RequestsViewModel(private val getApplication: Application) : AndroidViewModel(getApplication) {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _requestedRequests = MutableLiveData<ArrayList<Request>>()
    val requestedRequests: LiveData<ArrayList<Request>>
        get() = _requestedRequests

    private val _pendingRequests = MutableLiveData<ArrayList<Request>>()
    val pendingRequests: LiveData<ArrayList<Request>>
        get() = _pendingRequests

    private val _acceptHandlerResult = MutableLiveData<acceptHandlerResult>()
    val handlerResult: LiveData<acceptHandlerResult>
        get() = _acceptHandlerResult

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

    fun getPendingRequests() {
        val userId = FirebaseAuth.getInstance().uid
        val requestsArrayList = arrayListOf<Request>()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/requests/$userId/pending/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val request = it.getValue(Request::class.java)!!
                            requestsArrayList.add(request)
                        }
                        _pendingRequests.postValue(requestsArrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun getRequestedRequests() {
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
                        _requestedRequests.postValue(requestsArrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun acceptHandler(request: Request, choice: Boolean) {
        val userId = FirebaseAuth.getInstance().uid
        var requestedKey = ""
        var pendingKey = ""
        val lentReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/$userId/lent/")
        val oweReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${request.debt.toUser.uid}/owe/")
        val referenceDeleteRequested = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("requests/$userId/requested")
        val referenceDeletePending = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("requests/${request.debt.toUser.uid}/pending")

        viewModelScope.launch(Dispatchers.IO) {
            referenceDeleteRequested.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach{
                            val requestedRequest = it.getValue(Request::class.java)!!
                            if (requestedRequest.debt.debtId == request.debt.debtId) {
                                requestedKey = it.key!!
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        viewModelScope.launch(Dispatchers.IO) {
            referenceDeletePending.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach{
                            val pendingRequest = it.getValue(Request::class.java)!!
                            if (pendingRequest.debt.debtId == request.debt.debtId) {
                                pendingKey = it.key!!
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        referenceDeletePending.child("$pendingKey/").removeValue()
        referenceDeleteRequested.child("$requestedKey/").removeValue()
        if (choice == false) {
            oweReference.push().setValue(request.debt)
            lentReference.push().setValue(request.debt)
        }
    }
}