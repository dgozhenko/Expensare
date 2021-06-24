package com.example.expensare.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.io.Serializable

data class AdditionalUserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "add_user_entity_id")
    val id: Int,
    @ColumnInfo(name = "add_user_uid")
    val uid: String,
    @ColumnInfo(name = "add_username")
    val username: String,
    @ColumnInfo(name = "add_email")
    val email: String,
    @ColumnInfo(name = "add_user_avatar")
    val avatar: String?
): Serializable {
    companion object {
        val EMPTY: AdditionalUserEntity = AdditionalUserEntity(
            0,
            "",
            "",
            "",
            ""
        )
    }
}
