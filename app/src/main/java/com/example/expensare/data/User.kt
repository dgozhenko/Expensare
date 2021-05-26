package com.example.expensare.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@kotlinx.parcelize.Parcelize
data class User(val uid: String, val username: String, val avatar: Avatar) : Parcelable {
    constructor(): this("", "", Avatar(0))
}
