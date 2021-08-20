package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.models.Group
import com.example.domain.models.Response
import com.example.domain.models.SingleLiveEvent

class CreateGroup (private val groupRepository: GroupRepository) {

    suspend operator fun invoke(groupId: String, group: Group): LiveData<Response<String>> = groupRepository.create(groupId, group)
}