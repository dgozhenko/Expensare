package com.example.domain.database.daos

import androidx.room.*
import com.example.domain.database.entities.RequestEntity

@Dao
interface RequestDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE) fun createRequest(requestEntity: RequestEntity)

  @Delete fun deleteRequest(requestEntity: RequestEntity)

  @Query("SELECT * FROM request") fun getAllRequests(): List<RequestEntity>
}
