package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.models.util.Response
import com.example.domain.models.util.UserGroupData

class CreateUserInGroup(private val groupRepository: GroupRepository) {

    suspend operator fun invoke(userGroupData: UserGroupData): LiveData<Response<String>> = groupRepository.createUserInGroup(userGroupData)
}