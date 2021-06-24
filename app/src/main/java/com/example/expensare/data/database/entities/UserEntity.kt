package com.example.expensare.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensare.data.models.User
import java.io.Serializable

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_entity_id")
    val id: Int,
    @ColumnInfo(name = "user_uid")
    val uid: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "user_avatar")
    val avatar: String?
    ): Serializable {
    companion object {
        val EMPTY: UserEntity = UserEntity(
            0,
            "",
            "",
            "",
            ""
        )
    }
}
