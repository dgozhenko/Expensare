package com.example.expensare.di

import com.example.expensare.di.modules.AppModule
import com.example.expensare.di.modules.StorageModule
import com.example.expensare.di.modules.ViewModelsModule
import com.example.expensare.ui.addexpense.AddExpenseFragment
import com.example.expensare.ui.auth.avatar.AvatarPickerFragment
import com.example.expensare.ui.auth.avatar.ChooseNameFragment
import com.example.expensare.ui.dashboard.DashboardFragment
import com.example.expensare.ui.group.ChooseGroupFragment
import com.example.expensare.ui.group.CreateGroupFragment
import com.example.expensare.ui.manage_group.debts.GroupDebtFragment
import com.example.expensare.ui.manage_group.members.GroupMembersFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, StorageModule::class, ViewModelsModule::class])
interface ApplicationComponent {
    fun inject(fragment: AddExpenseFragment)
    fun inject(fragment: AvatarPickerFragment)
    fun inject(fragment: ChooseNameFragment)
    fun inject(fragment: DashboardFragment)
    fun inject(fragment: ChooseGroupFragment)
    fun inject(fragment: CreateGroupFragment)
    fun inject(fragment: GroupDebtFragment)
    fun inject(fragment: GroupMembersFragment)
}