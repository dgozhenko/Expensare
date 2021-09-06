package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.RequestInterface
import com.example.domain.models.Request
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import java.util.ArrayList

class RequestRepository(private val requestInterface: RequestInterface) {

    suspend fun getPendingRequests(): LiveData<Response<ArrayList<Request>>> =
        requestInterface.getPendingRequests()

    suspend fun getRequestedRequests(): LiveData<Response<ArrayList<Request>>> =
        requestInterface.getRequestedRequests()

    suspend fun acceptHandler(
        request: Request,
        choice: Boolean
    ): SingleLiveEvent<Response<String>> =
        requestInterface.acceptHandler(request, choice)
}