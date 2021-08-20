package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Response
import com.example.domain.models.UserGroupData

class CreateUserInGroup(private val groupRepository: GroupRepository) {

    suspend operator fun invoke(userGroupData: UserGroupData): LiveData<Response<String>> = groupRepository.createUserInGroup(userGroupData)
}