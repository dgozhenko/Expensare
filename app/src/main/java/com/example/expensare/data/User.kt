package com.example.expensare.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@kotlinx.parcelize.Parcelize
data class User(val uid: String, val username: String, val email: String, val avatar: String?) : Parcelable {
    constructor(): this("", "", "", null)
}
