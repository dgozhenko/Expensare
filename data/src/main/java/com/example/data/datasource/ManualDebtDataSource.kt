package com.example.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.interfaces.ManualDebtInterface
import com.example.domain.database.entities.ManualDebtEntity
import com.example.domain.database.entities.SecondUserEntity
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Debt
import com.example.domain.models.Request
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
                    response.value = Response.error("No lent manual debts", null)
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

    override suspend fun createRequest(debtEntity: Debt): SingleLiveEvent<Response<String>> {
        val response = SingleLiveEvent<Response<String>>()
        response.value = Response.loading(null)

        val pattern = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val newCalendar = Calendar.getInstance(Locale.getDefault())
        val neededDate = simpleDateFormat.format(newCalendar.time)
        val lentReference =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/manual_debts/${debtEntity.lentUser.uid}/lent/")
        val oweReference =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/manual_debts/${debtEntity.oweUser.uid}/owe/")
        val referenceAddRequested =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("requests/${debtEntity.lentUser.uid}/requested")
        val referenceAddPending =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("requests/${debtEntity.oweUser.uid}/pending")

        oweReference.child(debtEntity.id).removeValue().apply {
            refreshOweDebts()
        }
        lentReference.child(debtEntity.id).removeValue().apply {
            refreshLentDebts()
        }
        referenceAddPending.push().setValue(Request(debtEntity, neededDate))
        referenceAddRequested.push().setValue(Request(debtEntity, neededDate))
        response.value = Response.success(null)
        return response
    }

    override suspend fun deleteDebt(debt: Debt): SingleLiveEvent<Response<String>> {
        val userId = FirebaseAuth.getInstance().uid
        val response = SingleLiveEvent<Response<String>>()
        response.value = Response.loading(null)

        val referenceCreateHistory =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("history/$userId/")
        val referenceHistoryLent =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/history/$userId/lent/${debt.id}")
        val referenceHistoryOwe =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/history/${debt.oweUser.uid}/owe/${debt.id}")

        val lentReference =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/manual_debts/${debt.lentUser.uid}/lent/")
        val oweReference =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/manual_debts/${debt.oweUser.uid}/owe/")
        oweReference.child(debt.id).removeValue().apply {
            refreshOweDebts()
        }
        lentReference.child(debt.id).removeValue().apply {
            refreshLentDebts()
        }

        //Create History
        referenceCreateHistory.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                referenceHistoryLent.setValue(
                    Debt(
                        debt.lentUser,
                        debt.oweUser,
                        debt.lentAmount,
                        debt.name,
                        debt.date,
                        debt.id
                    )
                )
                referenceHistoryOwe.setValue(
                    Debt(
                        debt.lentUser,
                        debt.oweUser,
                        debt.lentAmount,
                        debt.name,
                        debt.date,
                        debt.id
                    )
                )
                response.value = Response.success("Success")
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = Response.error(error.message, null)
            }
        })
        response.value = Response.success(null)
        return response
    }
}