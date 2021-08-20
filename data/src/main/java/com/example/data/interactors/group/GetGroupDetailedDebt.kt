package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Response
import com.example.domain.models.UserDebt

class GetGroupDetailedDebt (private val groupRepository: GroupRepository) {
    suspend operator fun invoke(user: UserEntity, debtToMe: Boolean): LiveData<Response<ArrayList<UserDebt>>> = groupRepository.getGroupDetailedDebt(user, debtToMe)
}