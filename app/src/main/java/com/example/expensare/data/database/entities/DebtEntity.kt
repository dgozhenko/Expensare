package com.example.expensare.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "debt_local")
data class DebtEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "debt_entity_id")
    val id: Int,
    @Embedded
    val toUser: UserEntity,
    @Embedded
    val fromUser: AdditionalUserEntity,
    @ColumnInfo(name = "debt_entity_amount")
    val amount: Int
    ): Serializable {
    companion object {
        val EMPTY: DebtEntity = DebtEntity(
            0,
            UserEntity.EMPTY,
            AdditionalUserEntity.EMPTY,
            0
        )
    }
}
