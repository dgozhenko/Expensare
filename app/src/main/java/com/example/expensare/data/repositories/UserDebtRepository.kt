package com.example.expensare.data.repositories

import com.example.expensare.data.database.ExpensareDatabase
import com.example.expensare.data.database.entities.UserDebtEntity

class UserDebtRepository(database: ExpensareDatabase) {

    private val userDebtDao = database.userDebtDao()

    fun createUserDebt(userDebtEntity: UserDebtEntity) {
        userDebtDao.createUserDebt(userDebtEntity)
    }

    fun getAllUserDebts(): ArrayList<UserDebtEntity> {
        return userDebtDao.getAllUserDebt() as ArrayList<UserDebtEntity>
    }
}