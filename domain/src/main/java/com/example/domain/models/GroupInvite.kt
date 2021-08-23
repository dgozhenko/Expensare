package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupInvite(
    val message: String,
    val group: Group,
    val fromUser: User,
    val dateTime: String
): Parcelable {
    constructor(): this("", Group(), User(), "")
}
