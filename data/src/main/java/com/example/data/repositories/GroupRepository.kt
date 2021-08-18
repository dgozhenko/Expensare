package com.example.data.repositories

import com.example.data.interfaces.GroupInterface
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group

class GroupRepository(private val groupInterface: GroupInterface) {

    suspend fun getAllUsersFromGroup(groupEntity: Group): ArrayList<UserEntity> = groupInterface.getUsersFromGroup(groupEntity)

    suspend fun getGroupByGroupId(): Group = groupInterface.getGroupByGroupId()
}