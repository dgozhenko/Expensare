package com.example.data.repositories

import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.UserDebtEntity

class UserDebtRepository(database: ExpensareDatabase) {

    private val userDebtDao = database.userDebtDao()

    fun createUserDebt(userDebtEntity: UserDebtEntity) {
        userDebtDao.createUserDebt(userDebtEntity)
    }

    fun getAllUserDebts(): ArrayList<UserDebtEntity> {
        return userDebtDao.getAllUserDebt() as ArrayList<UserDebtEntity>
    }
}