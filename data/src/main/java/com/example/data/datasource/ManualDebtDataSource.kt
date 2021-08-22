package com.example.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.interfaces.ManualDebtInterface
import com.example.domain.database.entities.ManualDebtEntity
import com.example.domain.database.entities.SecondUserEntity
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Debt
import com.example.domain.models.User
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ManualDebtDataSource @Inject constructor() : ManualDebtInterface {

    override suspend fun getUserInfo(): LiveData<User> {
        TODO("Not yet implemented")
    }

    override suspend fun createDebt(
        debtFor: String,
        amount: Int,
        fromUser: User,
        toUser: User
    ): SingleLiveEvent<Response<String>> {
        val response = SingleLiveEvent<Response<String>>()
        response.value = Response.loading(null)

        val pattern = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val newCalendar = Calendar.getInstance(Locale.getDefault())
        val neededDate = simpleDateFormat.format(newCalendar.time)
        val debtId = UUID.randomUUID().toString()

        val referenceCheck =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("manual_debts/${fromUser.uid}/")
        val referenceAddLent =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("manual_debts/${fromUser.uid}/lent/$debtId")
        val referenceAddOwe =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("manual_debts/${toUser.uid}/owe/$debtId")
        referenceCheck.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                referenceAddLent.setValue(
                    Debt(
                        fromUser,
                        toUser,
                        amount,
                        debtFor,
                        neededDate,
                        debtId
                    )
                )
                referenceAddOwe.setValue(
                    Debt(
                        fromUser,
                        toUser,
                        amount,
                        debtFor,
                        neededDate,
                        debtId
                    )
                )
                response.value = Response.success(null)
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = Response.error(error.message, null)
            }
        })
        return response
    }

    override suspend fun getLentDebts(): LiveData<Response<ArrayList<Debt>>> {
        val response = MutableLiveData<Response<ArrayList<Debt>>>()
        response.value = Response.loading(null)

        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<Debt>()
        val manualDebtsLent =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/manual_debts/${userId}/lent/")

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
                    response.value = Response.error("No lent manunnnnnal debts", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = Response.error(error.message, null)
            }
        })
        return response
    }

    override suspend fun getOweDebts(): LiveData<Response<ArrayList<Debt>>> {
        val response = MutableLiveData<Response<ArrayList<Debt>>>()
        response.value = Response.loading(null)

        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<Debt>()
        debtsArrayList.clear()
        val manualDebtsOwe =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/manual_debts/${userId}/owe/")

        manualDebtsOwe.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val oweDebt =
                            it.getValue(Debt::class.java)!!
                        if (oweDebt.oweUser.uid== userId) {
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

    override suspend fun refreshLentDebts(): LiveData<Response<java.util.ArrayList<Debt>>> {
        val response = MutableLiveData<Response<ArrayList<Debt>>>()
        response.value = Response.loading(null)

        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<Debt>()
        val manualDebtsLent =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/manual_debts/${userId}/lent/")

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

    override suspend fun refreshOweDebts(): LiveData<Response<java.util.ArrayList<Debt>>> {
        val response = MutableLiveData<Response<ArrayList<Debt>>>()
        response.value = Response.loading(null)

        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<Debt>()
        debtsArrayList.clear()
        val manualDebtsOwe =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/manual_debts/${userId}/owe/")

        manualDebtsOwe.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val oweDebt =
                            it.getValue(Debt::class.java)!!
                        if (oweDebt.oweUser.uid== userId) {
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