package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.models.Group
import com.example.domain.models.User
import com.example.domain.models.util.Response

class DeclineInvite (private val groupRepository: GroupRepository) {

    suspend operator fun invoke(user: User, group: Group): LiveData<Response<String>> = groupRepository.declineInvite(user, group)
}