package com.example.domain.database.daos

import androidx.room.*
import com.example.domain.database.entities.ManualDebtEntity

@Dao
interface ManualDebtDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun createManualDebt(manualDebtEntity: ManualDebtEntity)

  @Query("SELECT * FROM manual_debt") fun getAllDebts(): List<ManualDebtEntity>

  @Delete fun deleteDebt(manualDebtEntity: ManualDebtEntity)
}
