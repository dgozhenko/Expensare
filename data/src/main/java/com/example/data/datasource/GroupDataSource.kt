package com.example.data.datasource

import com.example.data.interfaces.GroupInterface
import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class GroupDataSource @Inject constructor(private val database: ExpensareDatabase, private val storage: Storage) : GroupInterface {

    override suspend fun getUsersFromGroup(group: Group): ArrayList<UserEntity> {

        return group.users

    }

    // REFACTOR TO GROUP ID IN DATABASE NODE
    override suspend fun getGroupByGroupId(): Group {
        val groupId = storage.groupId
        val groups =
            FirebaseDatabase.getInstance(
                "https://expensare-default-rtdb.europe-west1.firebasedatabase.app/"
            )
                .getReference("/groups/$groupId/")

        val groupTask = groups.get()
        Tasks.await(groupTask)
        return groupTask.result!!.getValue(Group::class.java)!!
    }

}
