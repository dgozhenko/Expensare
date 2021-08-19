package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.ListInterface
import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.ListItemEntity
import com.example.domain.models.ListItem
import com.example.domain.models.Response

class ListItemRepository(private val listInterface: ListInterface) {

  suspend fun create(listItem: ListItem): LiveData<Response<String>> = listInterface.create(listItem)

  suspend fun getAll(): LiveData<Response<ArrayList<ListItem>>> = listInterface.getAll()

  suspend fun delete(listItem: ListItem): LiveData<Response<String>> = listInterface.delete(listItem)
}
