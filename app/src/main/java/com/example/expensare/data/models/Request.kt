package com.example.expensare.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Request(val key: String, val debtId: String, val toUser: User, val fromUser: User, val amount: Int, val debtFor: String, val date: String) : Parcelable {
    constructor(): this ("", "", User("", "", "", null), User("", "", "", null), 0, "", "")
}