package com.example.expensare.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "group")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_entity_id")
    val id: Int,
    @ColumnInfo(name = "group_entity_group_id")
    val groupID: String,
    @ColumnInfo(name = "group_entity_group_name")
    val groupName: String,
    @ColumnInfo(name = "group_entity_group_type")
    val groupType: String,
    @ColumnInfo(name = "group_entity_users")
    val users: MutableList<String>
    ): Serializable {
    companion object {
        val EMPTY: GroupEntity = GroupEntity(
            0,
            "",
            "",
            "",
            mutableListOf()
        )
    }
}
