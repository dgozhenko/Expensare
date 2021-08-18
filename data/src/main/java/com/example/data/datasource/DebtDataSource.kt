package com.example.data.datasource

import com.example.data.interfaces.DebtInterface
import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import com.example.domain.models.UserDebt
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class DebtDataSource
@Inject
constructor(private val database: ExpensareDatabase, private val storage: Storage) : DebtInterface {
  override suspend fun create(debt: UserDebt) {
    val groupId = storage.groupId
    var operationDone = false
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

    // try to get results and wait for all data
    val check = groupDebtsCheck.get()
    Tasks.await(check)
    // if it result exists for each result find needed data and modify it, else create new
    if (check.result!!.exists()) {
      check.result!!.children.forEach {
        val key = it.key
        val userDebt = it.getValue(UserDebt::class.java)!!
        if (userDebt.firstUser == debt.firstUser && userDebt.secondUser == debt.secondUser) {
          operationDone = true
          groupDebtsAdd
            .child("/$key/")
            .setValue(
              UserDebt(
                userDebt.firstUser,
                userDebt.secondUser,
                userDebt.firstUserAmount + debt.firstUserAmount,
                userDebt.secondUserAmount - debt.secondUserAmount,
                false
              )
            )
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
      }
    } else {
      groupDebts.setValue(
        UserDebt(
          debt.firstUser,
          debt.secondUser,
          debt.firstUserAmount,
          -debt.firstUserAmount,
          false
        )
      )
    }
  }
}
