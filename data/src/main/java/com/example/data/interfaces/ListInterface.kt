package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.models.GroupList
import com.example.domain.models.util.Response

interface ListInterface {

    suspend fun create(groupList: GroupList) : LiveData<Response<String>>

    suspend fun getAll(): LiveData<Response<ArrayList<GroupList>>>

    suspend fun delete(groupList: GroupList): LiveData<Response<String>>

}