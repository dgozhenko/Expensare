package com.example.expensare.data


import android.os.Parcelable


@kotlinx.parcelize.Parcelize
data class User(val uid: String, val username: String, val email: String, val avatar: String?) : Parcelable {
    constructor(): this("", "", "", null)
}
