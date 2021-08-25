package com.example.data.interactors.list

import androidx.lifecycle.LiveData
import com.example.data.repositories.ListItemRepository
import com.example.domain.models.GroupList
import com.example.domain.models.util.Response

class GetList (private val listItemRepository: ListItemRepository) {
    suspend operator fun invoke(): LiveData<Response<ArrayList<GroupList>>> = listItemRepository.getAll()
}