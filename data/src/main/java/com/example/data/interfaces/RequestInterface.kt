package com.example.data.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.models.Request
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import java.util.ArrayList

interface RequestInterface {
    suspend fun getPendingRequests(): LiveData<Response<ArrayList<Request>>>
    suspend fun getRequestedRequests(): LiveData<Response<ArrayList<Request>>>
    suspend fun acceptHandler(request: Request, choice: Boolean): SingleLiveEvent<Response<String>>
}