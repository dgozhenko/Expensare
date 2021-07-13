package com.example.expensare.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "list_item")
data class ListItemEntity(
    @PrimaryKey(autoGenerate = true)
    val listItemId: Int,
    @ColumnInfo(name = "itemStore")
    val itemStore: String,
    @ColumnInfo(name = "itemName")
    val itemName: String,
    @ColumnInfo(name = "itemQuantity")
    val itemQuantity: Int,
    @ColumnInfo(name = "itemType")
    val itemType: String,
    @ColumnInfo(name = "itemIsChecked")
    val itemIsChecked: Boolean,
    @Embedded
    val itemUser: UserEntity
) : Serializable {
    companion object {
        val EMPTY: ListItemEntity = ListItemEntity(
            0,
            "",
            "",
            0,
            "",
            false,
            UserEntity.EMPTY
        )
    }
}
