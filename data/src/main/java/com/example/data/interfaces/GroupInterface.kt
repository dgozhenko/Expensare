package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.models.*
import com.example.domain.models.util.Response
import com.example.domain.models.util.UserGroupData

interface GroupInterface {
  suspend fun getUsersFromGroup(group: Group): ArrayList<User>

  suspend fun getGroupByGroupId(): LiveData<Response<Group>>

  suspend fun create(groupId: String,group: Group): LiveData<Response<String>>

  suspend fun listenFor(user: User): LiveData<Response<ArrayList<String>>>

  suspend fun getAllGroups(groupIds: ArrayList<String>): LiveData<Response<ArrayList<Group>>>

  suspend fun getUserByEmail(email: String): LiveData<Response<User>>

  suspend fun addUserToGroup(user: User): LiveData<Response<UserGroupData>>

  suspend fun createUserInGroup(userGroupData: UserGroupData): LiveData<Response<String>>

  suspend fun getGroupDebts(users: ArrayList<User>, debtToMe: Boolean): LiveData<Response<ArrayList<GroupDebt>>>

  suspend fun getGroupDetailedDebt(user: User, debtToMe: Boolean): LiveData<Response<ArrayList<GroupDebt>>>
}
