package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User
import com.example.domain.models.util.Response
import com.example.domain.models.util.UserGroupData

class AddUserToGroup(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(user: User): LiveData<Response<UserGroupData>> = groupRepository.addUserToGroup(user)
}