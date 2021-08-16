package com.example.domain.models

import android.os.Parcelable
import com.example.domain.database.entities.SecondUserEntity
import com.example.domain.database.entities.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Debt(val toUser: UserEntity, val fromUser: SecondUserEntity, val amount: Int): Parcelable {
    constructor(): this (UserEntity.EMPTY, SecondUserEntity.EMPTY, 0)
}
