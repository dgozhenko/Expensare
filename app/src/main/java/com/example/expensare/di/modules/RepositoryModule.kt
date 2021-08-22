package com.example.expensare.di.modules

import com.example.data.datasource.*
import com.example.data.interactors.auth.LoginUser
import com.example.data.interactors.auth.RegisterUser
import com.example.data.interactors.auth.avatar.CreateUserInDatabase
import com.example.data.interactors.auth.avatar.UploadImage
import com.example.data.interactors.expenses.CreateExpense
import com.example.data.interactors.expenses.DownloadExpenses
import com.example.data.interactors.group.*
import com.example.data.interactors.list.CreateListItem
import com.example.data.interactors.list.DeleteListItem
import com.example.data.interactors.list.GetList
import com.example.data.interactors.manual_debts.*
import com.example.data.interactors.user.CreateUser
import com.example.data.interactors.user.DownloadUser
import com.example.data.interfaces.*
import com.example.data.repositories.*
import com.example.data.storage.Storage
import com.example.domain.database.ExpensareDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

  @Singleton
  @Provides
  fun providesRequestRepository(database: ExpensareDatabase): RequestRepository {
    return RequestRepository(database)
  }

  // Expenses

  @Singleton
  @Provides
  fun providesExpensesInterface(database: ExpensareDatabase, storage: Storage): ExpensesInterface {
    return ExpenseDataSource(database, storage)
  }

  @Singleton
  @Provides
  fun providesExpenseRepository(expensesInterface: ExpensesInterface): ExpenseRepository {
    return ExpenseRepository(expensesInterface)
  }

  @Singleton
  @Provides
  fun providesExpenseDataSource(database: ExpensareDatabase, storage: Storage): ExpenseDataSource {
    return ExpenseDataSource(database, storage)
  }

  @Singleton
  @Provides
  fun providesCreateExpense(expenseRepository: ExpenseRepository): CreateExpense {
    return CreateExpense(expenseRepository)
  }

  @Singleton
  @Provides
  fun providesDownloadExpense(expenseRepository: ExpenseRepository): DownloadExpenses {
    return DownloadExpenses(expenseRepository)
  }

  // Group

  @Singleton
  @Provides
  fun providesGroupInterface(database: ExpensareDatabase, storage: Storage): GroupInterface {
    return GroupDataSource(database, storage)
  }

  @Singleton
  @Provides
  fun providesGroupRepository(groupInterface: GroupInterface): GroupRepository {
    return GroupRepository(groupInterface)
  }

  @Singleton
  @Provides
  fun providesGroupDataSource(database: ExpensareDatabase, storage: Storage): GroupDataSource {
    return GroupDataSource(database, storage)
  }

  @Singleton
  @Provides
  fun providesGetAllUsersFromGroups(groupRepository: GroupRepository): GetAllUsersFromGroup {
    return GetAllUsersFromGroup(groupRepository)
  }

  @Singleton
  @Provides
  fun providesGetGroupByGroupId(groupRepository: GroupRepository): GetGroupByGroupId {
    return GetGroupByGroupId(groupRepository)
  }

  @Singleton
  @Provides
  fun providesGetGroups(groupRepository: GroupRepository): GetAllGroups {
    return GetAllGroups(groupRepository)
  }

  @Singleton
  @Provides
  fun providesCreateGroup(groupRepository: GroupRepository): CreateGroup {
    return CreateGroup(groupRepository)
  }

  @Singleton
  @Provides
  fun providesListenFor(groupRepository: GroupRepository): ListenForGroups {
    return ListenForGroups(groupRepository)
  }

  @Singleton
  @Provides
  fun providesCreateUserInGroup(groupRepository: GroupRepository): CreateUserInGroup {
    return CreateUserInGroup(groupRepository)
  }

  @Singleton
  @Provides
  fun providesAddUserToGroup(groupRepository: GroupRepository): AddUserToGroup {
    return AddUserToGroup(groupRepository)
  }

  @Singleton
  @Provides
  fun providesGetUserByEmail(groupRepository: GroupRepository): GetUserByEmail {
    return GetUserByEmail(groupRepository)
  }

  @Singleton
  @Provides
  fun providesGetGroupDebts(groupRepository: GroupRepository): GetGroupDebts {
    return GetGroupDebts(groupRepository)
  }

  @Singleton
  @Provides
  fun providesGetGroupDetailedDebts(groupRepository: GroupRepository): GetGroupDetailedDebt {
    return GetGroupDetailedDebt(groupRepository)
  }

  // Debt

  @Singleton
  @Provides
  fun providesDebtInterface(database: ExpensareDatabase, storage: Storage): DebtInterface {
    return DebtDataSource(database, storage)
  }

  @Singleton
  @Provides
  fun providesDebtRepository(debtInterface: DebtInterface): DebtRepository {
    return DebtRepository(debtInterface)
  }

  @Singleton
  @Provides
  fun providesDebtDataSource(database: ExpensareDatabase, storage: Storage): DebtDataSource {
    return DebtDataSource(database, storage)
  }

  @Singleton
  @Provides
  fun providesCreateDebt(debtRepository: DebtRepository): com.example.data.interactors.debt.CreateDebt {
    return com.example.data.interactors.debt.CreateDebt(debtRepository)
  }

  // User

  @Singleton
  @Provides
  fun providesUserInterface(database: ExpensareDatabase): UserInterface {
    return UserDataSource(database)
  }

  @Singleton
  @Provides
  fun providesUserDataSource(database: ExpensareDatabase): UserDataSource {
    return UserDataSource(database)
  }

  @Singleton
  @Provides
  fun providesUserRepository(userInterface: UserInterface): UserRepository {
    return UserRepository(userInterface)
  }

  @Singleton
  @Provides
  fun providesCreateUser(userRepository: UserRepository): CreateUser {
    return CreateUser(userRepository)
  }

  @Singleton
  @Provides
  fun providesDownloadUser(userRepository: UserRepository): DownloadUser {
    return DownloadUser(userRepository)
  }

  // Login Screen

  @Singleton
  @Provides
  fun providesAuthInterface(): AuthInterface {
    return AuthDataSource()
  }

  @Singleton
  @Provides
  fun providesAuthDataSource(): AuthDataSource {
    return AuthDataSource()
  }

  @Singleton
  @Provides
  fun providesAuthRepository(authInterface: AuthInterface): AuthRepository {
    return AuthRepository(authInterface)
  }

  @Singleton
  @Provides
  fun providesLoginUser(authRepository: AuthRepository): LoginUser {
    return LoginUser(authRepository)
  }

  @Singleton
  @Provides
  fun providesRegisterUser(authRepository: AuthRepository): RegisterUser {
    return RegisterUser(authRepository)
  }

  //Choose Name
  @Singleton
  @Provides
  fun providesChooseNameInterface(): ChooseNameInterface {
    return ChooseNameDataSource()
  }

  @Singleton
  @Provides
  fun providesChooseNameDataSource(): ChooseNameDataSource {
    return ChooseNameDataSource()
  }

  @Singleton
  @Provides
  fun providesChooseNameRepository(chooseNameInterface: ChooseNameInterface): ChooseNameRepository {
    return ChooseNameRepository(chooseNameInterface)
  }

  @Singleton
  @Provides
  fun providesCreateUserInDatabase(chooseNameRepository: ChooseNameRepository): CreateUserInDatabase {
    return CreateUserInDatabase(chooseNameRepository)
  }

  @Singleton
  @Provides
  fun providesUploadImage(chooseNameRepository: ChooseNameRepository): UploadImage {
    return UploadImage(chooseNameRepository)
  }


  // Grocery List

  @Singleton
  @Provides
  fun providesListInterface(database: ExpensareDatabase, storage: Storage): ListInterface {
    return ListDataSource(database, storage)
  }

  @Singleton
  @Provides
  fun providesListDataSource(database: ExpensareDatabase, storage: Storage): ListDataSource {
    return ListDataSource(database, storage)
  }

  @Singleton
  @Provides
  fun providesListRepository(listInterface: ListInterface): ListItemRepository {
    return ListItemRepository(listInterface)
  }

  @Singleton
  @Provides
  fun providesCreateListItem(listItemRepository: ListItemRepository): CreateListItem {
    return CreateListItem(listItemRepository)
  }

  @Singleton
  @Provides
  fun providesDeleteListItem(listItemRepository: ListItemRepository): DeleteListItem {
    return DeleteListItem(listItemRepository)
  }

  @Singleton
  @Provides
  fun providesGetList(listItemRepository: ListItemRepository): GetList {
    return GetList(listItemRepository)
  }


  //Manual Debts

  @Singleton
  @Provides
  fun providesManualDebtsInterface(): ManualDebtInterface {
    return ManualDebtDataSource()
  }

  @Singleton
  @Provides
  fun providesManualDebtDataSource(): ManualDebtDataSource {
    return ManualDebtDataSource()
  }

  @Singleton
  @Provides
  fun providesManualDebtRepository(manualDebtInterface: ManualDebtInterface): ManualDebtRepository {
    return ManualDebtRepository(manualDebtInterface)
  }

  @Singleton
  @Provides
  fun providesCreateManualDebt(manualDebtRepository: ManualDebtRepository): CreateDebt {
    return CreateDebt(manualDebtRepository)
  }

  @Singleton
  @Provides
  fun providesGetLentDebts(manualDebtRepository: ManualDebtRepository): GetLentDebts {
    return GetLentDebts(manualDebtRepository)
  }

  @Singleton
  @Provides
  fun providesGetOweDebts(manualDebtRepository: ManualDebtRepository): GetOweDebts {
    return GetOweDebts(manualDebtRepository)
  }

  @Singleton
  @Provides
  fun providesRefreshLentDebts(manualDebtRepository: ManualDebtRepository): RefreshLentDebts {
    return RefreshLentDebts(manualDebtRepository)
  }

  @Singleton
  @Provides
  fun providesRefreshOweDebts(manualDebtRepository: ManualDebtRepository): RefreshOweDebts {
    return RefreshOweDebts(manualDebtRepository)
  }
}
