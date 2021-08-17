package com.example.expensare.data.repositories

import com.example.expensare.data.database.ExpensareDatabase
import com.example.expensare.data.database.entities.GroupEntity

class GroupRepository(database: ExpensareDatabase) {
    private val groupDao = database.groupDao()

    suspend fun createGroup(groupEntity: GroupEntity) {
        groupDao.createGroup(groupEntity)
    }

    suspend fun getGroups(): ArrayList<GroupEntity> {
        return groupDao.getGroups() as ArrayList<GroupEntity>
    }

    fun createGroupInFirebase(groupEntity: GroupEntity) {

    }
}