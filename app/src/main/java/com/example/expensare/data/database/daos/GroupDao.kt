package com.example.expensare.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensare.data.database.entities.GroupEntity

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createGroup(groupEntity: GroupEntity)

    @Query("SELECT * FROM groups")
    suspend fun getGroups(): List<GroupEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun downloadAllGroups(groups: ArrayList<GroupEntity>)
}