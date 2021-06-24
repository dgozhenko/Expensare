package com.example.expensare.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense(val name: String, val amount: Int, val user: User, val groupId: String, val date: String) : Parcelable {
    constructor(): this("", 0, User("", "", "" , null), "", "")
}
