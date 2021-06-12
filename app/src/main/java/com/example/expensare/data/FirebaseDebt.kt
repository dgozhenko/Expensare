package com.example.expensare.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FirebaseDebt(val uid: String, val user: User, var amount: Int): Parcelable {
    constructor(): this ("", User("", "", "", null), 0)

}
