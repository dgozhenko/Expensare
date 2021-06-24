package com.example.expensare.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensare.data.database.entities.*
import com.example.expensare.util.Converters

@Database(
    entities =
        [
            AvatarEntity::class,
            DebtEntity::class,
            ExpenseEntity::class,
            GroupEntity::class,
            UserDebtEntity::class,
            UserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ExpensareDatabase : RoomDatabase()
