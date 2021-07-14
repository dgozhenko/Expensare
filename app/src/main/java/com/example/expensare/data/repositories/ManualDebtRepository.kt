package com.example.expensare.data.repositories

import com.example.expensare.data.database.ExpensareDatabase
import com.example.expensare.data.database.entities.ManualDebtEntity

class ManualDebtRepository(database: ExpensareDatabase) {

    private val manualDebtDao = database.manualDebtDao()

    fun createManualDebt(manualDebtEntity: ManualDebtEntity) {
        manualDebtDao.createManualDebt(manualDebtEntity)
    }

    fun deleteManualDebt(manualDebtEntity: ManualDebtEntity) {
        manualDebtDao.deleteDebt(manualDebtEntity)
    }

    fun getAllManualDebts(): ArrayList<ManualDebtEntity> {
        return manualDebtDao.getAllDebts() as ArrayList<ManualDebtEntity>
    }
}