package com.example.expensare.di.modules

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensare.di.ViewModelFactory
import com.example.expensare.di.ViewModelKey
import com.example.expensare.ui.addexpense.AddExpenseViewModel
import com.example.expensare.ui.auth.avatar.AvatarViewModel
import com.example.expensare.ui.auth.avatar.ChooseNameViewModel
import com.example.expensare.ui.auth.login.LoginViewModel
import com.example.expensare.ui.auth.registration.RegistrationViewModel
import com.example.expensare.ui.dashboard.DashboardViewModel
import com.example.expensare.ui.dashboard.list.AddToListViewModel
import com.example.expensare.ui.dashboard.list.ListViewModel
import com.example.expensare.ui.group.ChooseGroupViewModel
import com.example.expensare.ui.group.CreateGroupViewModel
import com.example.expensare.ui.manage_group.debts.GroupDebtViewModel
import com.example.expensare.ui.manage_group.members.GroupMembersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindsLoginViewModel(viewModel: LoginViewModel?) : ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(AddExpenseViewModel::class)
    abstract fun bindsAddExpenseViewModel(viewModel: AddExpenseViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(AvatarViewModel::class)
    abstract fun bindsAvatarViewModel(viewModel: AvatarViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ChooseNameViewModel::class)
    abstract fun bindsChooseNameViewModel(viewModel: ChooseNameViewModel?) : ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    abstract fun bindsRegistrationViewModel(viewModel: RegistrationViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindsDashboardViewModel(viewModel: DashboardViewModel?) : ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ChooseGroupViewModel::class)
    abstract fun bindsChooseGroupViewModel(viewModel: ChooseGroupViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CreateGroupViewModel::class)
    abstract fun bindsCreateGroupViewModel(viewModel: CreateGroupViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(GroupMembersViewModel::class)
    abstract fun bindsGroupMembersViewModel(viewModel: GroupMembersViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(GroupDebtViewModel::class)
    abstract fun bindsGroupDebtViewModel(viewModel: GroupDebtViewModel?) : ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(AddToListViewModel::class)
    abstract fun bindsAddToListViewModel(viewModel: AddToListViewModel?) : ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindsListViewModel(viewModel: ListViewModel?): ViewModel?
}