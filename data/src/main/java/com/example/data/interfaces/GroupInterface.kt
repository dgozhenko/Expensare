package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent
import com.example.domain.models.UserGroupData

interface GroupInterface {
  suspend fun getUsersFromGroup(group: Group): ArrayList<UserEntity>

  suspend fun getGroupByGroupId(): LiveData<Response<Group>>

  suspend fun create(groupId: String,group: Group): LiveData<Response<String>>

  suspend fun listenFor(userEntity: UserEntity): LiveData<Response<ArrayList<String>>>

  suspend fun getAllGroups(groupIds: ArrayList<String>): LiveData<Response<ArrayList<Group>>>

  suspend fun getUserByEmail(email: String): LiveData<Response<UserEntity>>

  suspend fun addUserToGroup(userEntity: UserEntity): LiveData<Response<UserGroupData>>

  suspend fun createUserInGroup(userGroupData: UserGroupData): LiveData<Response<String>>
}
