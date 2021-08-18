package com.example.data.interactors.group

import com.example.data.repositories.GroupRepository
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group

class GetAllUsersFromGroup(private val groupRepository: GroupRepository) {
  suspend operator fun invoke(groupEntity: Group): ArrayList<UserEntity> =
    groupRepository.getAllUsersFromGroup(groupEntity)
}
