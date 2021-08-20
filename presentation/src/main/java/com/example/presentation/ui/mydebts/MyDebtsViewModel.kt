package com.example.presentation.ui.mydebts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Group
import com.example.domain.models.Request
import com.example.domain.models.User
import com.example.data.storage.Storage
import com.example.domain.models.Debt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MyDebtsViewModel @Inject constructor(private val getApplication: Application) : AndroidViewModel(getApplication) {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _group = MutableLiveData<Group>()
    val group: LiveData<Group>
        get() = _group

    private val _lentDebts = MutableLiveData<ArrayList<Debt>>()
    val lentDebts: LiveData<ArrayList<Debt>>
        get() = _lentDebts

    private val _oweDebts = MutableLiveData<ArrayList<Debt>>()
    val oweDebts: LiveData<ArrayList<Debt>>
        get() = _oweDebts

    private val _updatedOweDebts = MutableLiveData<ArrayList<Debt>>()
    val updatedOweDebts: LiveData<ArrayList<Debt>>
        get() = _updatedOweDebts

    private val _updatedLentDebts = MutableLiveData<ArrayList<Debt>>()
    val updatedLentDebts: LiveData<ArrayList<Debt>>
        get() = _updatedLentDebts

    private val _refreshedLentDebts = MutableLiveData<ArrayList<Debt>>()
    val refreshedLentDebts: LiveData<ArrayList<Debt>>
        get() = _refreshedLentDebts

    private val _refreshedOweDebts = MutableLiveData<ArrayList<Debt>>()
    val refreshedOweDebts: LiveData<ArrayList<Debt>>
        get() = _refreshedOweDebts

    init {
        getUserInfo()
        getGroupByGroupId()
        getLentDebts()
        getOweDebts()
    }

    // TODO: 17.08.2021 Repository
    private fun getUserInfo() {
        val userId = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/users/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var userIsExist = false
                            snapshot.children.forEach {
                                val userInfo = it.getValue(User::class.java)
                                if (userInfo != null) {
                                    if (userInfo.uid == userId) {
                                        userIsExist = true
                                        _user.postValue(userInfo)
                                    }
                                }
                                if (!userIsExist) {
                                    _user.postValue(null)
                                }
                            }
                        } else {
                            _user.postValue(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    // TODO: 17.08.2021 Repository
    private fun getGroupByGroupId() {
        val storage = Storage(getApplication.baseContext)
        val groupId = storage.groupId
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/groups/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val groupInfo = it.getValue(Group::class.java)
                            if (groupInfo != null) {
                                if (groupInfo.groupID == groupId) {
                                    _group.postValue(groupInfo)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    // TODO: 17.08.2021 Repository
     fun refreshLentDebts() {
        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<Debt>()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${userId}/lent/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val lentDebt = it.getValue(Debt::class.java)!!
                            if (lentDebt.oweUser.uid == userId) {
                                debtsArrayList.add(lentDebt)
                            }
                        }
                        debtsArrayList.reverse()
                        _refreshedLentDebts.postValue(debtsArrayList)
                    } else {
                        _refreshedLentDebts.postValue(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    // TODO: 17.08.2021 Repository
          private fun getLentDebts(){
            val userId = FirebaseAuth.getInstance().uid
            val debtsArrayList = arrayListOf<Debt>()
            val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${userId}/lent/")
            viewModelScope.launch(Dispatchers.IO) {
                reference.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                val lentDebt = it.getValue(Debt::class.java)!!
                                if (lentDebt.oweUser.uid == userId) {
                                    debtsArrayList.add(lentDebt)
                                }
                            }
                            debtsArrayList.reverse()
                            _lentDebts.postValue(debtsArrayList)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

    // TODO: 17.08.2021 Repository
     private fun getOweDebts(){
         val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<Debt>()
         debtsArrayList.clear()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${userId}/owe/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val oweDebt = it.getValue(Debt::class.java)!!
                            if (oweDebt.lentUser.uid == userId) {
                                debtsArrayList.add(oweDebt)
                            }
                        }
                        debtsArrayList.reverse()
                        _oweDebts.postValue(debtsArrayList)
                    } else {
                        _oweDebts.postValue(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    // TODO: 17.08.2021 Repository
     fun refreshOweDebts(){
        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<Debt>()
        debtsArrayList.clear()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${userId}/owe/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val oweDebt = it.getValue(Debt::class.java)!!
                            if (oweDebt.lentUser.uid == userId) {
                                debtsArrayList.add(oweDebt)
                            }
                        }
                        debtsArrayList.reverse()
                        _refreshedOweDebts.postValue(debtsArrayList)
                    } else {
                        _refreshedOweDebts.postValue(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    // TODO: 17.08.2021 Repository
    private fun updateOweDebts(){
        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<Debt>()
        debtsArrayList.clear()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${userId}/owe/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val oweDebt = it.getValue(Debt::class.java)!!
                            if (oweDebt.lentUser.uid == userId) {
                                debtsArrayList.add(oweDebt)
                            }
                        }
                        debtsArrayList.reverse()
                        _updatedOweDebts.postValue(debtsArrayList)
                    } else {
                        _updatedOweDebts.postValue(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    // TODO: 17.08.2021 Repository
    private fun updateLentDebts() {
        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<Debt>()
        debtsArrayList.clear()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${userId}/lent/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val lentDebt = it.getValue(Debt::class.java)!!
                            if (lentDebt.oweUser.uid == userId) {
                                debtsArrayList.add(lentDebt)
                            }
                        }
                        debtsArrayList.reverse()
                        _updatedLentDebts.postValue(debtsArrayList)
                    } else {
                        _updatedLentDebts.postValue(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    // TODO: 17.08.2021 Repository
    fun createRequest(debt: Debt){
        val pattern = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val newCalendar = Calendar.getInstance(Locale.getDefault())
        val neededDate = simpleDateFormat.format(newCalendar.time)
        viewModelScope.launch(Dispatchers.IO) {
            val lentReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${debt.oweUser.uid}/lent/")
            val oweReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${debt.lentUser.uid}/owe/")
            val referenceAddRequested = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("requests/${debt.oweUser.uid}/requested")
            val referenceAddPending = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("requests/${debt.lentUser.uid}/pending")

            oweReference.child(debt.id).removeValue().apply {
                updateOweDebts()
            }
            lentReference.child(debt.id).removeValue().apply {
                updateLentDebts()
            }
            referenceAddPending.push().setValue(Request(debt, neededDate))
            referenceAddRequested.push().setValue(Request(debt, neededDate))
        }
    }
}