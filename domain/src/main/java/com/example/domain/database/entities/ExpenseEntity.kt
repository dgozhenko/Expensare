package com.example.domain.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "expense")
data class ExpenseEntity(
  @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "expenseId") val expenseId: String = "",
  @ColumnInfo(name = "expenseName") val expenseName: String = "",
  @ColumnInfo(name = "expenseAmount") val expenseAmount: Int = 0,
  @Embedded val expenseUser: UserEntity = UserEntity.EMPTY,
  @ColumnInfo(name = "expenseGroupId") val expenseGroupId: String = "",
  @ColumnInfo(name = "expenseDate") val expenseDate: String = "",
  val uploaded: Boolean = false,
) : Serializable {
  companion object {
    val EMPTY: ExpenseEntity = ExpenseEntity("", "", 0, UserEntity.EMPTY, "", "", false)
  }
}
