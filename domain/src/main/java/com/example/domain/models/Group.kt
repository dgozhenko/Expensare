package com.example.domain.models

import android.os.Parcelable
import com.example.domain.database.entities.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
  val groupID: String,
  val groupName: String,
  val groupType: String,
  val users: ArrayList<UserEntity>
) : Parcelable {
  constructor() : this("", "", "", arrayListOf())
}
