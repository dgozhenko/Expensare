package com.example.expensare.data.models

import android.os.Parcelable
import com.example.expensare.data.models.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class ManualDebt(val toUser: User, val fromUser: User, val amount: Int, val debtFor: String): Parcelable {
    constructor(): this (User("", "", "", null), User("", "", "", null), 0, "")
}