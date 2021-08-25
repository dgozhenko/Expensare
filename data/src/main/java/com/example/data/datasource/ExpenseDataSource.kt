package com.example.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.interfaces.ExpensesInterface
import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.models.Expense
import com.example.domain.models.util.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class ExpenseDataSource
@Inject
constructor(private val database: ExpensareDatabase, private val storage: Storage) :
  ExpensesInterface {

  override suspend fun create(expense: Expense): LiveData<Response<String>> {
    val response = MutableLiveData<Response<String>>()
    response.value = Response.loading(null)
    val expenses =
      FirebaseDatabase.getInstance(
          "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
        )
        .getReference("expenses/${expense.groupId}")
        .push()

    val createdExpense = expenses.setValue(expense)
    createdExpense
      .addOnSuccessListener { response.value = Response.success("Expense created") }
      .addOnFailureListener { response.value = Response.error(it.message!!, null) }
      .addOnCanceledListener {
        response.value = Response.error("Expense creation was canceled", null)
      }
    return response
  }

  override suspend fun getAll(): ArrayList<Expense> {
    TODO("Not yet implemented")
  }

  override suspend fun download(): LiveData<Response<ArrayList<Expense>>> {
    val response = MutableLiveData<Response<ArrayList<Expense>>>()
    response.value = Response.loading(null)
    val groupId = storage.groupId
    val expenses =
      FirebaseDatabase.getInstance(
          "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
        )
        .getReference("/expenses/$groupId/")

    expenses.addValueEventListener(
      object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          if (snapshot.exists()) {
            val expensesArrayList = arrayListOf<Expense>()
            snapshot.children.forEach {
              val result = it.getValue(Expense::class.java)
              if (result != null) {
                expensesArrayList.add(result)
              }
            }
            expensesArrayList.reverse()
            response.value = Response.success(expensesArrayList)
          } else {
            response.value = Response.error("There no expenses yet.", null)
          }
        }

        override fun onCancelled(error: DatabaseError) {
          response.value = Response.error(error.message, null)
        }
      }
    )

    return response
  }
}
