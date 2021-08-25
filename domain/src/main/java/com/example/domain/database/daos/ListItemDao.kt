package com.example.domain.database.daos

import androidx.room.*
import com.example.domain.database.entities.ListItemEntity

@Dao
interface ListItemDao {

  @Query("SELECT * FROM list_item") fun getAllItems(): List<ListItemEntity>

  @Insert(onConflict = OnConflictStrategy.IGNORE) fun createItem(listItemEntity: ListItemEntity)

  @Delete fun deleteItem(listItemEntity: ListItemEntity)
}
