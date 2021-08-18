package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.models.Response

interface GroupInterface {
    suspend fun getUsersFromGroup(group: Group): ArrayList<UserEntity>

    suspend fun getGroupByGroupId(): LiveData<Response<Group>>
}