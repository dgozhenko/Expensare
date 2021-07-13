package com.example.expensare.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensare.data.database.entities.ExpenseEntity

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createExpense(expenseEntity: ExpenseEntity)

    @Query("SELECT * FROM expense")
    fun getExpenses(): List<ExpenseEntity>
}