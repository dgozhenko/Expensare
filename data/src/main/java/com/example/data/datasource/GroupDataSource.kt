package com.example.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.interfaces.GroupInterface
import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.models.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

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
}
