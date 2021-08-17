package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ManualDebt(val debtId: String, val toUser: User, val fromUser: User, val amount: Int, val debtFor: String, val date: String): Parcelable {
    constructor(): this ("", User("", "", "", null), User("", "", "", null), 0, "", "")
}
