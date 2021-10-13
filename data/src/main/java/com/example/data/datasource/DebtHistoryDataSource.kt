package com.example.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.interfaces.DebtHistoryInterface
import com.example.domain.models.Debt
import com.example.domain.models.util.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class DebtHistoryDataSource @Inject constructor() : DebtHistoryInterface{

    override suspend fun getLentHistory(): LiveData<Response<ArrayList<Debt>>> {
        val response = MutableLiveData<Response<ArrayList<Debt>>>()
        response.value = Response.loading(null)

        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<Debt>()
        val manualDebtsLent =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/history/${userId}/lent/")

        manualDebtsLent.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val lentDebt = it.getValue(Debt::class.java)!!
                        if (lentDebt.lentUser.uid == userId) {
                            debtsArrayList.add(lentDebt)
                        }
                    }
                    debtsArrayList.reverse()
                    response.value = Response.success(debtsArrayList)
                } else {
                    response.value = Response.error("No lent manual debts", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = Response.error(error.message, null)
            }
        })
        return response
    }

    override suspend fun getOweHistory(): LiveData<Response<ArrayList<Debt>>> {
        val response = MutableLiveData<Response<ArrayList<Debt>>>()
        response.value = Response.loading(null)

        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<Debt>()
        debtsArrayList.clear()
        val manualDebtsOwe =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/history/${userId}/owe/")

        manualDebtsOwe.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val oweDebt =
                            it.getValue(Debt::class.java)!!
                        if (oweDebt.oweUser.uid == userId) {
                            debtsArrayList.add(oweDebt)
                        }
                    }
                    debtsArrayList.reverse()
                    response.value = Response.success(debtsArrayList)
                } else {
                    response.value = Response.error("No owe manual debts", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = Response.error(error.message, null)
            }
        })
        return response
    }
}