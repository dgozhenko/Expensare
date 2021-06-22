package com.example.expensare.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ManualDebt(val toUser: User, val fromUser: User, val amount: Int, val debtFor: String): Parcelable {
    constructor(): this (User("", "", "", null), User("", "", "", null), 0, "")
}
