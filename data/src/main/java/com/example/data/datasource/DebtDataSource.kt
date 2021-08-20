package com.example.data.datasource

import com.example.data.interfaces.DebtInterface
import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import com.example.domain.models.GroupDebt
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class DebtDataSource
@Inject
constructor(private val database: ExpensareDatabase, private val storage: Storage) : DebtInterface {

  override suspend fun create(debt: GroupDebt): SingleLiveEvent<Response<String>> {
    val response: SingleLiveEvent<Response<String>> = SingleLiveEvent()
    response.value = Response.loading(null)
    val groupId = storage.groupId
    // Get all firebase references
    val groupDebts =
      FirebaseDatabase.getInstance(
          "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
        )
        .getReference("group_debts/$groupId")
        .push()

    val groupDebtsCheck =
      FirebaseDatabase.getInstance(
          "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
        )
        .getReference("group_debts/$groupId/")

    val groupDebtsAdd =
      FirebaseDatabase.getInstance(
          "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
        )
        .getReference("group_debts/$groupId")

    groupDebtsCheck.addListenerForSingleValueEvent(object : ValueEventListener{
      override fun onDataChange(snapshot: DataSnapshot) {
        var operationDone = false
        if (snapshot.exists()) {
          snapshot.children.forEach {
            val key = it.key
            val userDebt = it.getValue(GroupDebt::class.java)!!
            if (userDebt.lentUser == debt.lentUser && userDebt.oweUser == debt.oweUser) {
              operationDone = true
              groupDebtsAdd.child("/$key/")
                .setValue(
                  GroupDebt(
                    userDebt.lentUser,
                    userDebt.oweUser,
                    userDebt.lentedAmount + debt.lentedAmount,
                    userDebt.owedAmount - debt.owedAmount,
                    expanded = false,
                    true
                  )
                ).addOnSuccessListener {
                  response.value = Response.success("Debt updated")
                }
                .addOnFailureListener { e ->
                  response.value = Response.error(e.message!!, null)
                }
                .addOnCanceledListener {
                  response.value = Response.error("Debt update canceled", null)
                }
            } else if (userDebt.lentUser == debt.oweUser && userDebt.oweUser == debt.lentUser) {
              operationDone = true
              groupDebtsAdd
                .child("/$key/")
                .setValue(
                  GroupDebt(
                    userDebt.lentUser,
                    userDebt.oweUser,
                    userDebt.lentedAmount - debt.lentedAmount,
                    userDebt.owedAmount + debt.owedAmount,
                    expanded = false,
                    true
                  )
                )
                .addOnSuccessListener {
                  response.value = Response.success("Debt updated")
                }
                .addOnFailureListener { e ->
                  response.value = Response.error(e.message!!, null)
                }
                .addOnCanceledListener {
                  response.value = Response.error("Debt update canceled", null)
                }
            }
          }

          if (!operationDone) {
            groupDebts.setValue(
              GroupDebt(
                debt.lentUser,
                debt.oweUser,
                debt.lentedAmount,
                -debt.lentedAmount,
                expanded = false,
                true
              )
            )
              .addOnSuccessListener {
                response.value = Response.success("Debt created")
              }
              .addOnFailureListener { e ->
                response.value = Response.error(e.message!!, null)
              }
              .addOnCanceledListener {
                response.value = Response.error("Debt creation canceled", null)
              }
          }

        } else {
          operationDone = true
          groupDebts.setValue(
            GroupDebt(
              debt.lentUser,
              debt.oweUser,
              debt.lentedAmount,
              -debt.lentedAmount,
              expanded = false,
              true
            )
          )
            .addOnSuccessListener {
              response.value = Response.success("Debt created")
            }
            .addOnFailureListener { e ->
              response.value = Response.error(e.message!!, null)
            }
            .addOnCanceledListener {
              response.value = Response.error("Debt creation canceled", null)
            }
        }
      }

      override fun onCancelled(error: DatabaseError) {
        response.value = Response.error(error.message, null)
      }

    })

    return response
  }
}
