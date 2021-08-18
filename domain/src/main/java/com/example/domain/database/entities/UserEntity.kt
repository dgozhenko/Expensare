package com.example.domain.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class UserEntity(
  @PrimaryKey(autoGenerate = false) val userId: Int = 0,
  @ColumnInfo(name = "userIdentifier") val userUidId: String = "",
  @ColumnInfo(name = "userEmail") val userEmail: String = "",
  @ColumnInfo(name = "username") val username: String = "",
  @ColumnInfo(name = "password") val password: String = "",
  @ColumnInfo(name = "avatar") val avatar: String? = ""
) : Serializable {
  companion object {
    val EMPTY: UserEntity = UserEntity(0, "", "", "", "", null)
  }
}
