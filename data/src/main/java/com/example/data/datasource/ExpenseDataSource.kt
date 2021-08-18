package com.example.data.datasource


import android.accounts.NetworkErrorException
import com.example.data.interfaces.ExpensesInterface

import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.ExpenseEntity

import com.google.android.gms.tasks.Tasks

import com.google.firebase.database.FirebaseDatabase
import java.io.IOException

import javax.inject.Inject
import kotlin.collections.ArrayList

class ExpenseDataSource @Inject constructor(private val database: ExpensareDatabase, private val storage: Storage): ExpensesInterface {
    override suspend fun create(expenseEntity: ExpenseEntity) {
        val expenses =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("expenses/${expenseEntity.expenseGroupId}")
                .push()
        val createdExpense = expenses.setValue(expenseEntity)
        Tasks.await(createdExpense)
    }

    override suspend fun getAll(): ArrayList<ExpenseEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun download(): ArrayList<ExpenseEntity> {

        val groupId = storage.groupId
        val expensesArrayList = arrayListOf<ExpenseEntity>()
        val expenses = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/expenses/$groupId/")
        val expenseTask = expenses.get()
        expenseTask.addOnFailureListener {
            // Error Handling
        }
        Tasks.await(expenseTask)
        val result = expenseTask.result
        if (result!!.exists()) {
            result.children.forEach {
                val expense = it.getValue(ExpenseEntity::class.java)
                if (expense != null) {
                    expensesArrayList.add(expense)
                }
            }
        } else {
            //stop
        }


        return expensesArrayList
    }
}