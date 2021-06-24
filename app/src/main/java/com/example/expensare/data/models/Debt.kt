package com.example.expensare.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Debt(val toUser: User, val fromUser: User, val amount: Int): Parcelable {
    constructor(): this (User("", "", "", null), User("", "", "", null), 0)
}
