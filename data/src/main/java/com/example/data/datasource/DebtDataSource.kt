package com.example.data.datasource

import com.example.data.interfaces.DebtInterface
import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import com.example.domain.models.UserDebt
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class DebtDataSource
@Inject
constructor(private val database: ExpensareDatabase, private val storage: Storage) : DebtInterface {

  override suspend fun create(debt: UserDebt): SingleLiveEvent<Response<String>> {
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
            val userDebt = it.getValue(UserDebt::class.java)!!
            if (userDebt.firstUser == debt.firstUser && userDebt.secondUser == debt.secondUser) {
              operationDone = true
              groupDebtsAdd.child("/$key/")
                .setValue(
                  UserDebt(
                    userDebt.firstUser,
                    userDebt.secondUser,
                    userDebt.firstUserAmount + debt.firstUserAmount,
                    userDebt.secondUserAmount - debt.secondUserAmount,
                    false
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
            } else if (userDebt.firstUser == debt.secondUser && userDebt.secondUser == debt.firstUser) {
              operationDone = true
              groupDebtsAdd
                .child("/$key/")
                .setValue(
                  UserDebt(
                    userDebt.firstUser,
                    userDebt.secondUser,
                    userDebt.firstUserAmount - debt.firstUserAmount,
                    userDebt.secondUserAmount + debt.firstUserAmount,
                    false
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
              UserDebt(
                debt.firstUser,
                debt.secondUser,
                debt.firstUserAmount,
                -debt.firstUserAmount,
                false
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
            UserDebt(
              debt.firstUser,
              debt.secondUser,
              debt.firstUserAmount,
              -debt.firstUserAmount,
              false
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
