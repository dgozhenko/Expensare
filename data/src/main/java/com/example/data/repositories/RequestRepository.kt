package com.example.data.repositories

import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.RequestEntity

class RequestRepository(database: ExpensareDatabase) {

    private val requestDao = database.requestDao()

    fun createRequest(requestEntity: RequestEntity) {
        requestDao.createRequest(requestEntity)
    }

    fun deleteRequest(requestEntity: RequestEntity) {
        requestDao.deleteRequest(requestEntity)
    }

    fun getAllRequests(): ArrayList<RequestEntity> {
        return requestDao.getAllRequests() as ArrayList<RequestEntity>
    }

}