package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.GroupInterface
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.models.Response

class GroupRepository(private val groupInterface: GroupInterface) {

    suspend fun getAllUsersFromGroup(groupEntity: Group): ArrayList<UserEntity> = groupInterface.getUsersFromGroup(groupEntity)

    suspend fun getGroupByGroupId(): LiveData<Response<Group>> = groupInterface.getGroupByGroupId()
}