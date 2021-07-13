package com.example.expensare.data.database.daos

import androidx.room.*
import com.example.expensare.data.database.entities.RequestEntity

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createRequest(requestEntity: RequestEntity)

    @Delete
    fun deleteRequest(requestEntity: RequestEntity)

    @Query("SELECT * FROM request")
    fun getAllRequests(): List<RequestEntity>

}