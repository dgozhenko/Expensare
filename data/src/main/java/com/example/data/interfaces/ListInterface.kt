package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.models.ListItem
import com.example.domain.models.Response

interface ListInterface {

    suspend fun create(listItem: ListItem) : LiveData<Response<String>>

    suspend fun getAll(): LiveData<Response<ArrayList<ListItem>>>

    suspend fun delete(listItem: ListItem): LiveData<Response<String>>

}