package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.models.Group
import com.example.domain.models.Response

class GetAllGroups(private val groupRepository: GroupRepository) {

    suspend operator fun invoke(groupIds: ArrayList<String>): LiveData<Response<ArrayList<Group>>> = groupRepository.getAllGroups(groupIds)
}