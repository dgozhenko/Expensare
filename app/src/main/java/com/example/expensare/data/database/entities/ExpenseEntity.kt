package com.example.expensare.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "expense")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Int,
    @ColumnInfo(name = "expenseName")
    val expenseName: String,
    @ColumnInfo(name = "expenseAmount")
    val expenseAmount: Int,
    @Embedded
    val expenseUser: UserEntity,
    @ColumnInfo(name = "expenseGroupId")
    val expenseGroupId: String,
    @ColumnInfo(name = "expenseDate")
    val expenseDate: String
) : Serializable {
    companion object {
        val EMPTY: ExpenseEntity = ExpenseEntity(
            0,
            "",
            0,
            UserEntity.EMPTY,
            "",
            ""
        )
    }
}