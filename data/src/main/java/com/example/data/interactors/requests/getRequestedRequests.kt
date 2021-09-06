package com.example.data.interactors.requests

import androidx.lifecycle.LiveData
import com.example.data.repositories.RequestRepository
import com.example.domain.models.Request
import com.example.domain.models.util.Response
import java.util.ArrayList

class getRequestedRequests(private val requestRepository: RequestRepository) {
    suspend operator fun invoke(): LiveData<Response<ArrayList<Request>>> =
        requestRepository.getRequestedRequests()
}