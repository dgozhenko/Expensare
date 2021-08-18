package com.example.domain.models

import android.os.Parcelable
import com.example.domain.database.entities.SecondUserEntity
import com.example.domain.database.entities.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDebt(
    val firstUser: UserEntity,
    val secondUser: UserEntity,
    val firstUserAmount: Int,
    val secondUserAmount: Int,
    var expanded: Boolean
) : Parcelable {
    constructor() : this(UserEntity.EMPTY, UserEntity.EMPTY, 0, 0, false)
}
