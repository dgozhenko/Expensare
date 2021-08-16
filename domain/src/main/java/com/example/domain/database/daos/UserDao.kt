package com.example.domain.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.database.entities.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
   suspend fun createUser(userEntity: UserEntity)

    @Query("select * from user")
   suspend fun getAllUsers(): List<UserEntity>

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun downloadUsers(users: ArrayList<UserEntity>)
}