package com.example.expensare.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDebt(val fullAmount: Int, val user: User, var expanded: Boolean): Parcelable {
    constructor(): this(0, User("", "", "" , null), false)
}