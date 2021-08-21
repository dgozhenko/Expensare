package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.util.Response
import com.example.domain.models.GroupDebt
import com.example.domain.models.User

class GetGroupDebts(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(users: ArrayList<User>, debtToMe: Boolean): LiveData<Response<ArrayList<GroupDebt>>> = groupRepository.getGroupDebts(users, debtToMe)
}