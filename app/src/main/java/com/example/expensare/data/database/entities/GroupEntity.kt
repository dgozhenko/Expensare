package com.example.expensare.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = false)
    val group: Int = 0,
    @ColumnInfo(name = "groupId")
    val groupUid: String = "",
    @ColumnInfo(name = "groupName")
    val groupName: String ="",
    @ColumnInfo(name = "groupType")
    val groupType: String = "",
    @ColumnInfo(name = "groupUsers")
    val groupUsers: ArrayList<String> = arrayListOf(),
) : Serializable {
    companion object {
        val EMPTY: GroupEntity = GroupEntity(
            0,
            "",
            "",
            "",
            arrayListOf()
        )
    }
}

