package com.example.domain.models

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class User(
  val uid: String,
  val username: String,
  val email: String,
  val avatar: String?,
  val groups: ArrayList<String>?,
  val groupInvites: ArrayList<GroupInvite>?
  ) : Parcelable {
  constructor() : this("", "", "", null, null, null)
}
