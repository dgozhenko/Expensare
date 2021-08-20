package com.example.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.interfaces.GroupInterface
import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.models.Response
import com.example.domain.models.UserGroupData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class GroupDataSource
@Inject
constructor(private val database: ExpensareDatabase, private val storage: Storage) :
  GroupInterface {

  override suspend fun getUsersFromGroup(group: Group): ArrayList<UserEntity> {
    return group.users
  }

  override suspend fun getGroupByGroupId(): LiveData<Response<Group>> {
    val groupData = MutableLiveData<Response<Group>>()
    val groupId = storage.groupId
    val groups =
      FirebaseDatabase.getInstance(
          "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
        )
        .getReference("/groups/$groupId")
    groupData.value = Response.loading(null)
    groups.addValueEventListener(
      object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          if (snapshot.exists()) {
            val result = snapshot.getValue(Group::class.java)
            groupData.value = Response.success(result)
          } else {
            groupData.value = Response.error("Group not found.", null)
          }
        }

        override fun onCancelled(error: DatabaseError) {
          groupData.value = Response.error(error.message, null)
        }
      }
    )
    return groupData
  }

  override suspend fun create(groupId: String, group: Group): LiveData<Response<String>> {
    val response = MutableLiveData<Response<String>>()
    response.value = Response.loading(null)

    var user: UserEntity = UserEntity.EMPTY
    group.users.forEach {
      user = it
    }

    val groups =
      FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("groups/$groupId/")
    val userRef = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
      .getReference("users/${user.userUidId}/")

    val groupsId = arrayListOf<String>()
    if (user.groups != null) {
      val userGroupIds = user.groups!!
      userGroupIds.forEach {
        groupsId.add(it)
      }
    }

    groups
      .setValue(group)
      .addOnCompleteListener {
        groupsId.add(groupId)
        userRef.setValue(
          UserEntity(userId = user.userId, userEmail = user.userEmail, username = user.username, userUidId = user.userUidId, groups = groupsId, avatar = user.avatar)
        )
          .addOnSuccessListener {
            response.value = Response.success("Group created.")
          }
          .addOnFailureListener { response.value =  Response.error(it.message!!, null) }
          .addOnCanceledListener { response.value = Response.error("Group creation canceled", null) }
      }
      .addOnFailureListener { response.value =  Response.error(it.message!!, null) }
      .addOnCanceledListener { response.value =  Response.error("Group creation canceled", null) }
    return response
  }

  override suspend fun listenFor(userEntity: UserEntity): LiveData<Response<ArrayList<String>>> {
    val response = MutableLiveData<Response<ArrayList<String>>>()
    response.value = Response.loading(null)
    val allGroups = arrayListOf<String>()
    val groups = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
      .getReference("users/${userEntity.userUidId}").child("groups")
      groups.addValueEventListener(object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
          if (snapshot.exists()) {
            snapshot.children.forEach {
              val group = it.getValue(String::class.java)
              if (group != null) {
                allGroups.add(group)
              }
            }
            response.value = Response.success(allGroups)
          } else {
            response.value = Response.error("No groups was found", null)
          }
        }

        override fun onCancelled(error: DatabaseError) {
          response.value = Response.error(error.message, null)
        }

      })
    return response
  }

  override suspend fun getAllGroups(groupIds: ArrayList<String>): LiveData<Response<ArrayList<Group>>> {
    val response = MutableLiveData<Response<ArrayList<Group>>>()
    response.value = Response.loading(null)
    val groupsArray = arrayListOf<Group>()
    groupIds.forEach {
      val groups = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("groups/$it")
      groups.addValueEventListener(object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
          if (snapshot.exists()) {
            val groupObject = snapshot.getValue(Group::class.java)
            if (groupObject != null) {
              groupsArray.add(groupObject)
              response.value = Response.success(groupsArray)
            }
          } else {
            response.value = Response.error("There no groups", null)
          }
        }

        override fun onCancelled(error: DatabaseError) {
          response.value = Response.error(error.message, null)
        }
      })
    }
    return response
  }

  override suspend fun getUserByEmail(email: String): LiveData<Response<UserEntity>> {
    val response = MutableLiveData<Response<UserEntity>>()
    response.value = Response.loading(null)

    val user =
      FirebaseDatabase.getInstance(
        "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("/users/")

      user.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
              snapshot.children.forEach {
                val userInfo = it.getValue(UserEntity::class.java)
                if (userInfo != null) {
                  if (userInfo.userEmail == email) {
                    response.value = Response.success(userInfo)
                  }
                }
              }
            } else {
              response.value = Response.error("No user was found", null)
            }
          }

          override fun onCancelled(error: DatabaseError) {
              response.value = Response.error(error.message, null)
          }
        })
    return response
  }

  override suspend fun addUserToGroup(userEntity: UserEntity): LiveData<Response<UserGroupData>> {
    val response = MutableLiveData<Response<UserGroupData>>()
    response.value = Response.loading(null)
    val groupId = storage.groupId
    val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/groups/")
    reference.addListenerForSingleValueEvent((object : ValueEventListener{
      override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
          snapshot.children.forEach {
            val group = it.getValue(Group::class.java)
            if (group != null) {
              if (group.groupID == groupId) {
                val groupKey = it.key
                val usersIdArray = group.users
                val userGroupData = UserGroupData(user = userEntity, groupKey = groupKey!!, groupId = usersIdArray)
                response.value = Response.success(userGroupData)
              }
            }
          }
        } else {
          response.value = Response.error("There no groups", null)
        }
      }

      override fun onCancelled(error: DatabaseError) {
        response.value = Response.error(error.message, null)
      }

    }))
    return response
  }

  override suspend fun createUserInGroup(
    userGroupData: UserGroupData
  ): LiveData<Response<String>> {
    val response = MutableLiveData<Response<String>>()
    response.value = Response.loading(null)
    val usersIdArray = userGroupData.groupId
    val user = userGroupData.user
    val groupKey = userGroupData.groupKey
    val groupsId = arrayListOf<String>()
    if (user.groups != null) {
      val userGroupIds = user.groups!!
      userGroupIds.forEach {
        groupsId.add(it)
      }
    }
    usersIdArray.add(user)
    val userRef = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
      .getReference("users/${user.userUidId}/")
    val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/groups/")
    reference.child(groupKey).child("users").setValue(usersIdArray)
      .addOnSuccessListener {
        groupsId.add(groupKey)
        userRef.setValue(
          UserEntity(userId = user.userId, userEmail = user.userEmail, username = user.username, userUidId = user.userUidId, groups = groupsId, avatar = user.avatar)
        )
          .addOnSuccessListener {
            response.value = Response.success("User added to group")
          }
          .addOnFailureListener { response.value =  Response.error(it.message!!, null) }
          .addOnCanceledListener { response.value = Response.error("Group creation canceled", null) }
      }
      .addOnFailureListener {
        response.value = Response.error(it.message!!, null)
      }
      .addOnCanceledListener {
        response.value = Response.error("User creation in group was canceled", null)
      }
    return response
  }
}
