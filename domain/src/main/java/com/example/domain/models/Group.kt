package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(val groupID: String, val groupName: String, val groupType: String, val users: ArrayList<String>): Parcelable {
    constructor(): this("","", "", arrayListOf())
}
