package com.example.domain.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "userDebt")
data class UserDebtEntity(
  @PrimaryKey(autoGenerate = false) val userDebt: Int,
  @Embedded val userDebtFirstUser: UserEntity,
  @Embedded val userDebtSecondUser: SecondUserEntity,
  @ColumnInfo(name = "userDebtFirstUserAmount") val userDebtFirstUserAmount: Int,
  @ColumnInfo(name = "userDebtSecondUserAmount") val userDebtSecondUserAmount: Int,
  @ColumnInfo(name = "userDebtExpanded") val userDebtExpanded: Boolean
) : Serializable {
  companion object {
    val EMPTY: UserDebtEntity =
      UserDebtEntity(0, UserEntity.EMPTY, SecondUserEntity.EMPTY, 0, 0, false)
  }
}
