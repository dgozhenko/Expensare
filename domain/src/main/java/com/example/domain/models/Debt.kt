package com.example.domain.models

import android.os.Parcelable
import com.example.domain.database.entities.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Debt(
  val lentUser: User,
  val oweUser: User,
  val lentAmount: Int,
  val oweAmount: Int,
  val name: String,
  val date: String,
  val id: String
  ) : Parcelable {
  constructor() : this(User(), User(), 0, 0, "", "", "")
}
