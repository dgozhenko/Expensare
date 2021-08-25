package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.models.GroupInvite
import com.example.domain.models.GroupInvites
import com.example.domain.models.User
import com.example.domain.models.util.Response

class GetAllInvites (private val groupRepository: GroupRepository) {
    suspend operator fun invoke(user: User): LiveData<Response<GroupInvites>> = groupRepository.getAllInvites(user)
}