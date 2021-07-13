package com.example.expensare.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "manual_debt")
data class ManualDebtEntity(
    @PrimaryKey(autoGenerate = true)
    val manualDebt: Int,
    @ColumnInfo(name = "manualDebtId")
    val manualDebtId: Int,
    @Embedded
    val manualDebtToUser: UserEntity,
    @Embedded
    val manualDebtFromUser: SecondUserEntity,
    @ColumnInfo(name = "manualDebtAmount")
    val manualDebtAmount: Int,
    @ColumnInfo(name = "manualDebtFor")
    val manualDebtFor: String,
    @ColumnInfo(name = "manualDebtDate")
    val manualDebtDate: String
) : Serializable {
    companion object {
        val EMPTY : ManualDebtEntity = ManualDebtEntity(
            0,
            0,
            UserEntity.EMPTY,
            SecondUserEntity.EMPTY,
            0,
            "",
            ""
        )
    }
}
