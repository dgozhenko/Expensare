package com.example.expensare.data.models

import android.os.Parcelable
import com.example.expensare.data.database.entities.SecondUserEntity
import com.example.expensare.data.database.entities.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Debt(val toUser: UserEntity, val fromUser: SecondUserEntity, val amount: Int): Parcelable {
    constructor(): this (UserEntity.EMPTY, SecondUserEntity.EMPTY, 0)
}
