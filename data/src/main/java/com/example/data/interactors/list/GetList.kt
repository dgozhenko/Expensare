package com.example.data.interactors.list

import androidx.lifecycle.LiveData
import com.example.data.repositories.ListItemRepository
import com.example.domain.models.ListItem
import com.example.domain.models.Response

class GetList (private val listItemRepository: ListItemRepository) {
    suspend operator fun invoke(): LiveData<Response<ArrayList<ListItem>>> = listItemRepository.getAll()
}