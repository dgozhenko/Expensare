package com.example.data.repositories

import com.example.domain.database.ExpensareDatabase
import com.example.domain.database.entities.ManualDebtEntity

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
