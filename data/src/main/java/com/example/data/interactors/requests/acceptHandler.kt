package com.example.data.interactors.requests

import com.example.data.repositories.RequestRepository
import com.example.domain.models.Request
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent

class acceptHandler(private val requestRepository: RequestRepository) {
    suspend operator fun invoke(request: Request, choice: Boolean): SingleLiveEvent<Response<String>> =
        requestRepository.acceptHandler(request, choice)
}