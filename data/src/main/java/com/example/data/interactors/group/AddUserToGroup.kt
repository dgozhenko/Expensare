package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Response
import com.example.domain.models.UserGroupData

class AddUserToGroup(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(userEntity: UserEntity): LiveData<Response<UserGroupData>> = groupRepository.addUserToGroup(userEntity)
}