package com.example.expensare.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(val groupName: String, val groupType: String, val users: ArrayList<String>): Parcelable {
    constructor(): this("", "", arrayListOf())
}