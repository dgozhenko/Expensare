package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.GroupInterface
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent

class GroupRepository(private val groupInterface: GroupInterface) {

  suspend fun getAllUsersFromGroup(groupEntity: Group): ArrayList<UserEntity> =
    groupInterface.getUsersFromGroup(groupEntity)

  suspend fun getGroupByGroupId(): LiveData<Response<Group>> = groupInterface.getGroupByGroupId()

  suspend fun create(groupId: String, group: Group): LiveData<Response<String>> = groupInterface.create(groupId,group)

  suspend fun listenFor(userEntity: UserEntity): LiveData<Response<ArrayList<String>>> = groupInterface.listenFor(userEntity)

  suspend fun getAllGroups(groupIds: ArrayList<String>): LiveData<Response<ArrayList<Group>>> = groupInterface.getAllGroups(groupIds)
}
