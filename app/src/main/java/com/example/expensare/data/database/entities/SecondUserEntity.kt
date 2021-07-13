package com.example.expensare.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "second_user")
data class SecondUserEntity(
    @PrimaryKey(autoGenerate = true)
    val secondUser: Int,
    @ColumnInfo(name = "secondUserId")
    val secondUserUidId: String,
    @ColumnInfo(name = "secondUsername")
    val secondUsername: String,
    @ColumnInfo(name = "secondPassword")
    val secondPassword: String,
    @ColumnInfo(name = "secondAvatar")
    val secondAvatar: String?
) : Serializable {
    companion object {
        val EMPTY: SecondUserEntity = SecondUserEntity(
            0,
            "",
            "",
            "",
            null
        )
    }
}

