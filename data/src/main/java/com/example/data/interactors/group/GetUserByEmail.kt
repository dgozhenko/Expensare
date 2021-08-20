package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Response


class GetUserByEmail(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(email: String): LiveData<Response<UserEntity>> = groupRepository.getUserByEmail(email)
}