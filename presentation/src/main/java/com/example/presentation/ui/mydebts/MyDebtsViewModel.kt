package com.example.presentation.ui.mydebts

import androidx.lifecycle.*
import com.example.data.interactors.group.GetGroupByGroupId
import com.example.data.interactors.manual_debts.GetLentDebts
import com.example.data.interactors.manual_debts.GetOweDebts
import com.example.data.interactors.user.DownloadUser
import com.example.domain.database.entities.UserEntity
import com.example.domain.models.Group
import com.example.domain.database.entities.ManualDebtEntity
import com.example.domain.models.Debt
import com.example.domain.models.User
import com.example.domain.models.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MyDebtsViewModel @Inject constructor(
    private val getGroupByGroupId: GetGroupByGroupId,
    private val downloadUser: DownloadUser,
    private val getLentDebts: GetLentDebts,
    private val getOweDebts: GetOweDebts
) :
    ViewModel() {

    private val _user = MutableLiveData<Response<User>>()
    val user: LiveData<Response<User>>
        get() = _user

    private val _group = MutableLiveData<Response<Group>>()
    val group: LiveData<Response<Group>>
        get() = _group

    private val _lentDebts = MutableLiveData<Response<ArrayList<Debt>>>()
    val lentDebts: LiveData<Response<ArrayList<Debt>>>
        get() = _lentDebts

    private val _oweDebts = MutableLiveData<Response<ArrayList<Debt>>>()
    val oweDebts: LiveData<Response<ArrayList<Debt>>>
        get() = _oweDebts

    private val _refreshedLentDebts = MutableLiveData<Response<ArrayList<Debt>>>()
    val refreshedLentDebts: LiveData<Response<ArrayList<Debt>>>
        get() = _refreshedLentDebts

    private val _refreshedOweDebts = MutableLiveData<Response<ArrayList<Debt>>>()
    val refreshedOweDebts: LiveData<Response<ArrayList<Debt>>>
        get() = _refreshedOweDebts

    init {
        getUserInfo()
        getGroupByGroupId()
        getLentDebts()
        getOweDebts()
        refreshLentDebts()
        refreshOweDebts()
    }

    // TODO: 17.08.2021 Repository
    private fun getUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            downloadUser.invoke().observeForever { _user.postValue(it) }
        }
    }

    // TODO: 17.08.2021 Repository
    private fun getGroupByGroupId() {
        viewModelScope.launch(Dispatchers.Main) {
            getGroupByGroupId.invoke().observeForever { _group.postValue(it) }
        }
    }


    // TODO: 17.08.2021 Repository
    private fun getLentDebts() {
        viewModelScope.launch(Dispatchers.Main) {
            getLentDebts.invoke().observeForever { _lentDebts.postValue(it) }
        }
    }

    // TODO: 17.08.2021 Repository
    private fun getOweDebts() {
        viewModelScope.launch(Dispatchers.Main) {
            getOweDebts.invoke().observeForever { _oweDebts.postValue(it) }
        }
    }

    private fun refreshLentDebts() {
        viewModelScope.launch(Dispatchers.Main) {
            getLentDebts.invoke().observeForever { _refreshedLentDebts.postValue(it) }
        }
    }

    private fun refreshOweDebts() {
        viewModelScope.launch(Dispatchers.Main) {
            getOweDebts.invoke().observeForever { _refreshedOweDebts.postValue(it) }
        }
    }

    /*// TODO: 17.08.2021 Repository
    fun createRequest(debtEntity: ManualDebtEntity) {
        val pattern = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val newCalendar = Calendar.getInstance(Locale.getDefault())
        val neededDate = simpleDateFormat.format(newCalendar.time)
        viewModelScope.launch(Dispatchers.IO) {
            val lentReference =
                FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("/manual_debts/${debtEntity.fromUser.uid}/lent/")
            val oweReference =
                FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("/manual_debts/${debtEntity.toUser.uid}/owe/")
            val referenceAddRequested =
                FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("requests/${debtEntity.fromUser.uid}/requested")
            val referenceAddPending =
                FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("requests/${debtEntity.toUser.uid}/pending")

            oweReference.child(debtEntity.debtId).removeValue().apply {
                updateOweDebts()
            }
            lentReference.child(debtEntity.debtId).removeValue().apply {
                updateLentDebts()
            }
            referenceAddPending.push().setValue(Request(debtEntity, neededDate))
            referenceAddRequested.push().setValue(Request(debtEntity, neededDate))
        }
    }*/
}