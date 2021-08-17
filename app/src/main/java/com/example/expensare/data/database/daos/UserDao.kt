package com.example.expensare.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensare.data.database.entities.UserEntity
import com.example.expensare.data.models.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
   suspend fun createUser(userEntity: UserEntity)

    @Query("select * from user")
   suspend fun getAllUsers(): List<UserEntity>

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun downloadUsers(users: ArrayList<UserEntity>)
}