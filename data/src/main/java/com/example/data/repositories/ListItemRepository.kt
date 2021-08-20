package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.ListInterface
import com.example.domain.models.GroupList
import com.example.domain.models.util.Response

class ListItemRepository(private val listInterface: ListInterface) {

  suspend fun create(groupList: GroupList): LiveData<Response<String>> = listInterface.create(groupList)

  suspend fun getAll(): LiveData<Response<ArrayList<GroupList>>> = listInterface.getAll()

  suspend fun delete(groupList: GroupList): LiveData<Response<String>> = listInterface.delete(groupList)
}
