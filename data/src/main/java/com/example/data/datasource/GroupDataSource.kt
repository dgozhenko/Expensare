package com.example.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.interfaces.GroupInterface
import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.*
import com.example.domain.models.util.Response
import com.example.domain.models.util.UserGroupData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class GroupDataSource @Inject constructor(private val database: ExpensareDatabase, private val storage: Storage) : GroupInterface {

  override suspend fun getUsersFromGroup(group: Group): ArrayList<User> {
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

    var user: User = User()
    group.users.forEach {
      user = it
    }

    val groups =
      FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("groups/$groupId/")
    val userRef = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
      .getReference("users/${user.uid}/")

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
          User(username = user.username, email = user.email, uid = user.uid, avatar = user.avatar, groups = groupsId, groupInvites = null)
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

  override suspend fun listenFor(user: User): LiveData<Response<ArrayList<String>>> {
    val response = MutableLiveData<Response<ArrayList<String>>>()
    response.value = Response.loading(null)
    val allGroups = arrayListOf<String>()
    val groups = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
      .getReference("users/${user.uid}").child("groups")
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

  override suspend fun createGroupInvite(
    user: User,
    group: Group,
    dateTime: String
  ): LiveData<Response<String>> {
    val response = MutableLiveData<Response<String>> ()
    response.value = Response.loading(null)

    val userRef = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
      .getReference("users/${user.uid}/")
    val uid = FirebaseAuth.getInstance().uid
    var fromUser = User()
    var userAlreadyInGroup = false

    group.users.forEach {
      if (it.uid == uid) {
        fromUser = it
      } else if (it.uid == user.uid) {
        userAlreadyInGroup = true
      }
    }
    if (!userAlreadyInGroup) {
      val groupInvites: java.util.ArrayList<GroupInvite>
      var userAlreadyInvited = false
      val invite = GroupInvite(message = "You was invited", fromUser = fromUser, group = group, dateTime = dateTime)
      if (user.groupInvites != null) {
        groupInvites = user.groupInvites!!
        groupInvites.forEach { groupInvite ->
        if (groupInvite.group.groupID == group.groupID) {
          userAlreadyInvited = true
         }
        }
        groupInvites.add(invite)
      } else {
        groupInvites = arrayListOf()
        groupInvites.add(invite)
      }

      if (!userAlreadyInvited) {
        userRef.setValue(
          User(username = user.username, email = user.email, uid = user.uid, avatar = user.avatar, groups = user.groups, groupInvites = groupInvites)
        )
          .addOnSuccessListener {
            response.value = Response.success("User invited to group")
          }
          .addOnFailureListener { response.value =  Response.error(it.message!!, null) }
          .addOnCanceledListener { response.value = Response.error("Invite canceled", null) }
      } else {
        response.value = Response.error("User already invited in ${group.groupName}", null)
      }

    } else {
      response.value = Response.error("User already in ${group.groupName}", null)
    }


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

  override suspend fun getUserByEmail(email: String): LiveData<Response<User>> {
    val response = MutableLiveData<Response<User>>()
    response.value = Response.loading(null)
    var userFound = false
    val user =
      FirebaseDatabase.getInstance(
        "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("/users/")

      user.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
              snapshot.children.forEach {
                val userInfo = it.getValue(User::class.java)
                if (userInfo != null) {
                  if (userInfo.email == email) {
                    response.value = Response.success(userInfo)
                    userFound = true
                  }
                }
              }
              if (!userFound) {
                response.value = Response.error("There no user with that email - $email", null)
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

  override suspend fun addUserToGroup(user: User, group: Group): LiveData<Response<UserGroupData>> {
    val response = MutableLiveData<Response<UserGroupData>>()
    response.value = Response.loading(null)
    val groupId = group.groupID
    val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/groups/$groupId")
    reference.addListenerForSingleValueEvent((object : ValueEventListener{
      override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
            val group = snapshot.getValue(Group::class.java)
            if (group != null) {
              if (group.groupID == groupId) {
                val groupKey = snapshot.key
                val usersIdArray = group.users
                val userGroupData = UserGroupData(user = user, groupKey = groupKey!!, groupId = usersIdArray, group = group)
                response.value = Response.success(userGroupData)
              }
            }
        } else {
          response.value = Response.error("There no group", null)
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
    val refreshedInvites = arrayListOf<GroupInvite>()
    val groupInvites = user.groupInvites
    groupInvites?.forEach {
      if (it.group.groupID != userGroupData.group.groupID) {
          refreshedInvites.add(it)
      }
    }
    if (user.groups != null) {
      val userGroupIds = user.groups!!
      userGroupIds.forEach {
        groupsId.add(it)
      }
    }
    usersIdArray.add(user)
    val userRef = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
      .getReference("users/${user.uid}/")
    val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/groups/")
    reference.child(groupKey).child("users").setValue(usersIdArray)
      .addOnSuccessListener {
        groupsId.add(groupKey)
        userRef.setValue(
          User(username = user.username, email = user.email, uid = user.uid, avatar = user.avatar, groups = groupsId, groupInvites = refreshedInvites)
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

  override suspend fun getGroupDebts(users: ArrayList<User>, debtToMe: Boolean): LiveData<Response<ArrayList<GroupDebt>>> {
    val response = MutableLiveData<Response<ArrayList<GroupDebt>>>()
    response.value = Response.loading(null)

    val groupId = storage.groupId
    var fullAmount = 0
    val userDebtArrayList = arrayListOf<GroupDebt>()
    users.forEach { groupUser ->
      if (debtToMe) {
        val referenceCheck = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("group_debts/$groupId/")
        referenceCheck.addListenerForSingleValueEvent(
          object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              if (snapshot.exists()) {
                snapshot.children.forEach {
                  val userDebt = it.getValue(GroupDebt::class.java)!!
                  if (userDebt.lentUser.uid == groupUser.uid && userDebt.lentedAmount > 0) {
                    fullAmount += userDebt.lentedAmount
                  } else if (userDebt.oweUser.uid == groupUser.uid && userDebt.owedAmount > 0) {
                    fullAmount += userDebt.owedAmount
                  }
                }
                if (fullAmount != 0) {
                  userDebtArrayList.add(GroupDebt(groupUser, groupUser, fullAmount, fullAmount, expanded = false, true))
                  fullAmount = 0
                  response.value = Response.success(userDebtArrayList)
                }
              } else {
                response.value = Response.error("No group debts was found", null)
              }
            }

            override fun onCancelled(error: DatabaseError) {
              response.value = Response.error(error.message, null)
            }
          }
        )
      } else {
        val referenceCheck = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("group_debts/$groupId/")
        referenceCheck.addListenerForSingleValueEvent(
          object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              if (snapshot.exists()) {
                snapshot.children.forEach {
                  val userDebt = it.getValue(GroupDebt::class.java)!!
                  if (userDebt.lentUser.uid == groupUser.uid && userDebt.lentedAmount < 0) {
                    fullAmount += userDebt.lentedAmount
                  } else if (userDebt.oweUser.uid == groupUser.uid && userDebt.owedAmount < 0) {
                    fullAmount += userDebt.owedAmount
                  }
                }
                if (fullAmount != 0) {
                  userDebtArrayList.add(GroupDebt(groupUser, groupUser, fullAmount * -1, fullAmount * -1, expanded = false, true))
                  fullAmount = 0
                  response.value = Response.success(userDebtArrayList)
                }
              } else {
                response.value = Response.error("No group debts was found", null)
              }
            }

            override fun onCancelled(error: DatabaseError) {
              response.value = Response.error(error.message, null)
            }
          }
        )
      }
    }
    return response
  }

  override suspend fun getGroupDetailedDebt(user: User, debtToMe: Boolean): LiveData<Response<ArrayList<GroupDebt>>> {
    val response = MutableLiveData<Response<ArrayList<GroupDebt>>>()
    response.value = Response.loading(null)
    val groupId = storage.groupId
    val userDebtArrayList = arrayListOf<GroupDebt>()
    if (debtToMe) {
      val referenceCheck = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("group_debts/$groupId/")
      referenceCheck.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
              snapshot.children.forEach {
                val userDebt = it.getValue(GroupDebt::class.java)!!
                if (userDebt.lentUser.uid == user.uid && userDebt.lentedAmount > 0) {
                  // we need secondUser
                  userDebtArrayList.add(GroupDebt(userDebt.oweUser, userDebt.oweUser, userDebt.owedAmount, userDebt.owedAmount, expanded = false, true))
                } else if (userDebt.oweUser.uid == user.uid && userDebt.owedAmount > 0) {
                  // we need firsUser
                  userDebtArrayList.add(GroupDebt(userDebt.lentUser, userDebt.lentUser, userDebt.lentedAmount, userDebt.lentedAmount, expanded = false, true))
                }
              }
              response.value = Response.success(userDebtArrayList)
            } else {
              response.value = Response.error("There no debts", null)
            }
          }

          override fun onCancelled(error: DatabaseError) {
            response.value = Response.error(error.message, null)
          }
        }
      )
    } else {
      val referenceCheck = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("group_debts/$groupId/")
      referenceCheck.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
              snapshot.children.forEach {
                val userDebt = it.getValue(GroupDebt::class.java)!!
                if (userDebt.lentUser.uid == user.uid && userDebt.lentedAmount < 0) {
                  // we need secondUser
                  userDebtArrayList.add(GroupDebt(userDebt.oweUser, userDebt.oweUser, userDebt.owedAmount, userDebt.owedAmount, expanded = false, true))
                } else if (userDebt.oweUser.uid == user.uid && userDebt.owedAmount < 0) {
                  // we need firstUser
                  userDebtArrayList.add(GroupDebt(userDebt.lentUser, userDebt.lentUser, userDebt.lentedAmount, userDebt.lentedAmount, expanded = false, true))
                }
              }
              response.value = Response.success(userDebtArrayList)
            } else {
              response.value = Response.error("There no debts", null)
            }
          }

          override fun onCancelled(error: DatabaseError) {
            response.value = Response.error(error.message, null)
          }
        }
      )
    }
    return response
  }

  override suspend fun getAllInvites(user: User): LiveData<Response<GroupInvites>> {
    val response = MutableLiveData<Response<GroupInvites>>()
    response.value = Response.loading(null)
    val userRef = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
      .getReference("users/${user.uid}").child("groupInvites")
    val invitesArray = GroupInvites()
    userRef.addListenerForSingleValueEvent(object : ValueEventListener{
      override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
            snapshot.children.forEach {
              val result = it.getValue(GroupInvite::class.java)!!
              invitesArray.add(result)
            }
          response.value = Response.success(invitesArray)
        } else {
          response.value = Response.error("There no invites", null)
        }
      }

      override fun onCancelled(error: DatabaseError) {
        response.value = Response.error(error.message, null)
      }
    })
    return response
  }

  override suspend fun declineInvite(user: User, group: Group): LiveData<Response<String>> {
    val response = MutableLiveData<Response<String>>()
    response.value = Response.loading(null)
    val refreshedInvites = arrayListOf<GroupInvite>()
    val groupInvites = user.groupInvites
    groupInvites?.forEach {
      if (it.group.groupID != group.groupID) {
          refreshedInvites.add(it)
      }
    }

    val userRef = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
      .getReference("users/${user.uid}/")
    userRef.setValue(
      User(username = user.username, email = user.email, uid = user.uid, avatar = user.avatar, groups = user.groups, groupInvites = refreshedInvites)
    )
      .addOnSuccessListener {
        response.value = Response.success("User declined invite to group")
      }
      .addOnFailureListener { response.value =  Response.error(it.message!!, null) }
      .addOnCanceledListener { response.value = Response.error("Group creation canceled", null) }
    return response
  }
}
