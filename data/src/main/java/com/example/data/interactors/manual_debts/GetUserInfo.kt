package com.example.data.interactors.manual_debts

import androidx.lifecycle.LiveData
import com.example.data.repositories.ManualDebtRepository
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.User

class GetUserInfo(private val manualDebtRepository: ManualDebtRepository) {
    suspend operator fun invoke(): LiveData<User> =
        manualDebtRepository.getUserInfo()
}