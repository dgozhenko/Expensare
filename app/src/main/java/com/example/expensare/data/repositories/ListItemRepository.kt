package com.example.expensare.data.repositories

import com.example.expensare.data.database.ExpensareDatabase
import com.example.expensare.data.database.entities.ListItemEntity

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