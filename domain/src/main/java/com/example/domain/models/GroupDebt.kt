package com.example.domain.models

import android.os.Parcelable
import com.example.domain.database.entities.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupDebt(
  val lentUser: User,
  val oweUser: User,
  val lentedAmount: Int,
  val owedAmount: Int,
  var expanded: Boolean,
  val uploaded: Boolean
) : Parcelable {
  constructor() : this(User(), User(), 0, 0, false, false)
}
