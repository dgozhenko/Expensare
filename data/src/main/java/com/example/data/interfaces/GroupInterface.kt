package com.example.data.interfaces

import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group

interface GroupInterface {
    suspend fun getUsersFromGroup(group: Group): ArrayList<UserEntity>

    suspend fun getGroupByGroupId(): Group
}