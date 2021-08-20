package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent

interface GroupInterface {
  suspend fun getUsersFromGroup(group: Group): ArrayList<UserEntity>

  suspend fun getGroupByGroupId(): LiveData<Response<Group>>

  suspend fun create(groupId: String,group: Group): LiveData<Response<String>>

  suspend fun listenFor(userEntity: UserEntity): LiveData<Response<ArrayList<String>>>

  suspend fun getAllGroups(groupIds: ArrayList<String>): LiveData<Response<ArrayList<Group>>>
}
