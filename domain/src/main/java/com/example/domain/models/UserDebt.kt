package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDebt(
    val firstUser: User,
    val secondUser: User,
    val firstUserAmount: Int,
    val secondUserAmount: Int,
    var expanded: Boolean
) : Parcelable {
    constructor() : this(User("", "", "", null), User("", "", "", null), 0, 0, false)
}
