package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.models.Group
import com.example.domain.models.User
import com.example.domain.models.util.Response

class CreateGroupInvite (private val groupRepository: GroupRepository) {

    suspend operator fun invoke(user: User, group: Group, dateTime: String): LiveData<Response<String>> = groupRepository.createGroupInvite(user, group, dateTime)
}