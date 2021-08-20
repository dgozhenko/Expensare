package com.example.domain.models

import android.os.Parcelable
import com.example.domain.database.entities.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserGroupData(
    val groupKey: String,
    val user: UserEntity,
    val groupId: ArrayList<UserEntity>
) : Parcelable {
    constructor() : this("",  UserEntity.EMPTY,  arrayListOf())
}
