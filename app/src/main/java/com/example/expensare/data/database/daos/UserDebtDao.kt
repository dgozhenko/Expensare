package com.example.expensare.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensare.data.database.entities.UserDebtEntity

@Dao
interface UserDebtDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createUserDebt(userDebtEntity: UserDebtEntity)

    @Query("select * from userDebt")
    fun getAllUserDebt(): List<UserDebtEntity>
}