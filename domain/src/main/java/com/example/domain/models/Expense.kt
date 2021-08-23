package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense(
  val expenseId: String,
  val name: String,
  val amount: Int,
  val user: User,
  val groupId: String,
  val date: String,
  val uploaded: Boolean
) : Parcelable {
  constructor() : this("","", 0, User("", "", "", null, null, null), "", "", false)
}
