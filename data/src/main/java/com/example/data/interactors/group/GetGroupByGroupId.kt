package com.example.data.interactors.group

import androidx.lifecycle.LiveData
import com.example.data.repositories.GroupRepository
import com.example.domain.models.Group
import com.example.domain.models.util.Response

class GetGroupByGroupId(private val groupRepository: GroupRepository) {

  suspend operator fun invoke(): LiveData<Response<Group>> = groupRepository.getGroupByGroupId()
}
