package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.GroupInterface
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.*
import com.example.domain.models.util.Response
import com.example.domain.models.util.UserGroupData

class GroupRepository(private val groupInterface: GroupInterface) {

  suspend fun getAllUsersFromGroup(groupEntity: Group): ArrayList<User> =
    groupInterface.getUsersFromGroup(groupEntity)

  suspend fun getGroupByGroupId(): LiveData<Response<Group>> = groupInterface.getGroupByGroupId()

  suspend fun create(groupId: String, group: Group): LiveData<Response<String>> = groupInterface.create(groupId,group)

  suspend fun listenFor(user: User): LiveData<Response<ArrayList<String>>> = groupInterface.listenFor(user)

  suspend fun getAllGroups(groupIds: ArrayList<String>): LiveData<Response<ArrayList<Group>>> = groupInterface.getAllGroups(groupIds)

  suspend fun getUserByEmail(email: String): LiveData<Response<User>> = groupInterface.getUserByEmail(email)

  suspend fun addUserToGroup(user: User, group: Group): LiveData<Response<UserGroupData>> = groupInterface.addUserToGroup(user, group)

  suspend fun createUserInGroup(userGroupData: UserGroupData): LiveData<Response<String>> = groupInterface.createUserInGroup(userGroupData)

  suspend fun getGroupDebts(users: ArrayList<User>, debtToMe: Boolean): LiveData<Response<ArrayList<GroupDebt>>> = groupInterface.getGroupDebts(users, debtToMe)

  suspend fun getGroupDetailedDebt(user: User, debtToMe: Boolean): LiveData<Response<ArrayList<GroupDebt>>> = groupInterface.getGroupDetailedDebt(user, debtToMe)

  suspend fun createGroupInvite(user: User, group: Group, dateTime: String): LiveData<Response<String>> = groupInterface.createGroupInvite(user, group, dateTime)

  suspend fun getAllInvites(user: User): LiveData<Response<GroupInvites>> = groupInterface.getAllInvites(user)

  suspend fun declineInvite(user: User, group: Group): LiveData<Response<String>> = groupInterface.declineInvite(user, group)
}
