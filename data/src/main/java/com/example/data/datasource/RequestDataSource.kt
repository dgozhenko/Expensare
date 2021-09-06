package com.example.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.interfaces.RequestInterface
import com.example.domain.models.Debt
import com.example.domain.models.Request
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

class RequestDataSource @Inject constructor() : RequestInterface {

    override suspend fun getPendingRequests(): LiveData<Response<ArrayList<Request>>> {
        val response = MutableLiveData<Response<ArrayList<Request>>>()
        response.value = Response.loading(null)

        val userId = FirebaseAuth.getInstance().uid
        val requestsArrayList = arrayListOf<Request>()
        val reference =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/requests/$userId/pending/")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val request = it.getValue(Request::class.java)!!
                        requestsArrayList.add(request)
                    }
                    response.value = Response.success(requestsArrayList)
                } else {
                    response.value = Response.error("No pending requests", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = Response.error(error.message, null)
            }
        })
        return response
    }

    override suspend fun getRequestedRequests(): LiveData<Response<ArrayList<Request>>> {
        val response = MutableLiveData<Response<ArrayList<Request>>>()
        response.value = Response.loading(null)

        val userId = FirebaseAuth.getInstance().uid
        val requestsArrayList = arrayListOf<Request>()
        val reference =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/requests/$userId/requested/")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val request = it.getValue(Request::class.java)!!
                        requestsArrayList.add(request)
                    }
                    response.value = Response.success(requestsArrayList)
                } else {
                    response.value = Response.error("No requested requests", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = Response.error(error.message, null)
            }
        })
        return response
    }

    override suspend fun acceptHandler(
        request: Request,
        choice: Boolean
    ): SingleLiveEvent<Response<String>> {
        val response = SingleLiveEvent<Response<String>>()
        response.value = Response.loading(null)

        val userId = FirebaseAuth.getInstance().uid
        var requestedKey = ""
        var pendingKey = ""
        val lentReference =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/manual_debts/$userId/lent/")
        val oweReference =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/manual_debts/${request.debt.oweUser.uid}/owe/")
        val referenceDeleteRequested =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("requests/$userId/requested")
        val referenceDeletePending =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("requests/${request.debt.oweUser.uid}/pending")

        referenceDeleteRequested.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val requestedRequest = it.getValue(Request::class.java)!!
                        if (requestedRequest.debt.id == request.debt.id) {
                            requestedKey = it.key!!
                        }
                    }
                } else {
                    response.value = Response.error("No requests", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = Response.error(error.message, null)
            }
        })

        referenceDeletePending.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val pendingRequest = it.getValue(Request::class.java)!!
                        if (pendingRequest.debt.id == request.debt.id) {
                            pendingKey = it.key!!
                        }
                    }
                } else {
                    response.value = Response.error("No requests", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = Response.error(error.message, null)
            }
        })

        referenceDeletePending.child("$pendingKey/").removeValue()
        referenceDeleteRequested.child("$requestedKey/").removeValue()
        if (choice == false) {
            oweReference.push().setValue(request.debt)
            lentReference.push().setValue(request.debt)
        }
        response.value = Response.success("Success")
        return response
    }
}