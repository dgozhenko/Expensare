package com.example.domain.models.util

import android.os.Parcelable
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserGroupData(
    val groupKey: String,
    val user: User,
    val groupId: ArrayList<User>
) : Parcelable {
    constructor() : this("",  User(),  arrayListOf())
}
