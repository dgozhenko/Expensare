package com.example.expensare.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensare.data.database.daos.*
import com.example.expensare.data.database.entities.*
import com.example.expensare.util.Converters

@Database(
    entities =
        [
            ExpenseEntity::class,
            GroupEntity::class,
            ListItemEntity::class,
            ManualDebtEntity::class,
            RequestEntity::class,
            SecondUserEntity::class,
            UserDebtEntity::class,
            UserEntity::class],
    version = 14,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ExpensareDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun groupDao(): GroupDao
    abstract fun listItemDo(): ListItemDao
    abstract fun manualDebtDao(): ManualDebtDao
    abstract fun requestDao(): RequestDao
    abstract fun userDao(): UserDao
    abstract fun userDebtDao(): UserDebtDao
}
