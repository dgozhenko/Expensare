package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Response
import com.example.domain.models.UserDebt

class GetGroupDebts(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(users: ArrayList<UserEntity>, debtToMe: Boolean): LiveData<Response<ArrayList<UserDebt>>> = groupRepository.getGroupDebts(users, debtToMe)
}