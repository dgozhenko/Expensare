package com.example.expensare.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensare.data.models.User
import kotlinx.parcelize.RawValue
import java.io.Serializable

@Entity(tableName = "manual_expense")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_entity_id")
    val id: Int,
    @ColumnInfo(name = "expense_name")
    val name: String,
    @ColumnInfo(name = "expense_entity_amount")
    val amount: Int,
    @Embedded
    val user: UserEntity,
    @ColumnInfo(name = "expense_entity_group_id")
    val groupId: String,
    @ColumnInfo(name = "expense_entity_date")
    val date: String
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
