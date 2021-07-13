package com.example.expensare.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensare.data.database.entities.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createUser(userEntity: UserEntity)

    @Query("select * from user")
    fun getAllUsers(): List<UserEntity>
}