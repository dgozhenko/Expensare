package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.models.Response

class ListenForGroups (private val groupRepository: GroupRepository) {
    suspend operator fun invoke(userEntity: UserEntity): LiveData<Response<ArrayList<String>>> = groupRepository.listenFor(userEntity)
}