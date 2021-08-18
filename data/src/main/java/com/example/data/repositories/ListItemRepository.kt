package com.example.data.repositories

import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.ListItemEntity

class ListItemRepository(database: ExpensareDatabase) {

  private val listItemDao = database.listItemDo()

  fun createListItem(listItemEntity: ListItemEntity) {
    listItemDao.createItem(listItemEntity)
  }

  fun deleteListItem(listItemEntity: ListItemEntity) {
    listItemDao.deleteItem(listItemEntity)
  }

  fun getAllListItems(): ArrayList<ListItemEntity> {
    return listItemDao.getAllItems() as ArrayList<ListItemEntity>
  }
}
