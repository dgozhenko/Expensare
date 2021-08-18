package com.example.data.interactors.group

import com.example.data.repositories.GroupRepository
import com.example.domain.models.Group

class GetGroupByGroupId (private val groupRepository: GroupRepository) {

    suspend operator fun invoke(): Group = groupRepository.getGroupByGroupId()

}