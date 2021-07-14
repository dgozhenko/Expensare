package com.example.expensare.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "request")
data class RequestEntity(
    @PrimaryKey(autoGenerate = false)
    val request: Int,
    @Embedded
    val requestDebt: ManualDebtEntity,
    @ColumnInfo(name = "requestDebtDate")
    val requestDebtDate: String
) : Serializable {
    companion object {
        val EMPTY : RequestEntity = RequestEntity(
            0,
            ManualDebtEntity.EMPTY,
            ""
        )
    }
}

