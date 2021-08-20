package com.example.domain.models

import android.os.Parcelable
import com.example.domain.database.entities.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupList(
  val store: String,
  val quantity: Int,
  val name: String,
  val type: String,
  val isChecked: Boolean,
  val user: User
) : Parcelable {
  constructor() : this("", 0, "", "", false, User())
}
