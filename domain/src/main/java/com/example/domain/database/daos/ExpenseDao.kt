package com.example.domain.database.daos

import androidx.room.*
import com.example.domain.database.entities.ExpenseEntity

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createExpense(expenseEntity: ExpenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun downloadExpenses(expenses: ArrayList<ExpenseEntity>)

    @Query("SELECT * FROM expense")
    suspend fun getExpenses(): List<ExpenseEntity>

    @Query("UPDATE expense SET uploaded = :uploaded")
    suspend fun updateUploadInfo(uploaded: Boolean)
}