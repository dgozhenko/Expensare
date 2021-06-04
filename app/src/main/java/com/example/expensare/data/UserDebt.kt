package com.example.expensare.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDebt(val fullAmount: Int, val fromUser: User, val toUser: User): Parcelable {
    constructor(): this(0, User("", "", "" , null), User("", "", "" , null))
}