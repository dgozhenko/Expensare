package com.example.expensare.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_debt")
data class UserDebtEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_debt_entity_id")
    val id: Int,
    @Embedded
    val firstUser: UserEntity,
    @Embedded
    val secondUser: AdditionalUserEntity,
    @ColumnInfo(name = "user_debt_entity_first_amount")
    val firstUserAmount: Int,
    @ColumnInfo(name = "user_debt_entity_second_amount")
    val secondUserAmount: Int,
    @ColumnInfo(name = "user_debt_entity_expanded")
    var expanded: Boolean
    ) : Serializable {
    companion object {
        val EMPTY: UserDebtEntity = UserDebtEntity(
            0,
            UserEntity.EMPTY,
            AdditionalUserEntity.EMPTY,
            0,
            0,
            false
        )
    }
}
