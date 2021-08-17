package com.example.presentation.ui.mydebts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Group
import com.example.domain.models.ManualDebt
import com.example.domain.models.Request
import com.example.domain.models.User
import com.example.data.storage.Storage
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

    private val _lentDebts = MutableLiveData<ArrayList<ManualDebt>>()
    val lentDebts: LiveData<ArrayList<ManualDebt>>
        get() = _lentDebts

    private val _oweDebts = MutableLiveData<ArrayList<ManualDebt>>()
    val oweDebts: LiveData<ArrayList<ManualDebt>>
        get() = _oweDebts

    private val _updatedOweDebts = MutableLiveData<ArrayList<ManualDebt>>()
    val updatedOweDebts: LiveData<ArrayList<ManualDebt>>
        get() = _updatedOweDebts

    private val _updatedLentDebts = MutableLiveData<ArrayList<ManualDebt>>()
    val updatedLentDebts: LiveData<ArrayList<ManualDebt>>
        get() = _updatedLentDebts

    private val _refreshedLentDebts = MutableLiveData<ArrayList<ManualDebt>>()
    val refreshedLentDebts: LiveData<ArrayList<ManualDebt>>
        get() = _refreshedLentDebts

    private val _refreshedOweDebts = MutableLiveData<ArrayList<ManualDebt>>()
    val refreshedOweDebts: LiveData<ArrayList<ManualDebt>>
        get() = _refreshedOweDebts

    init {
        getUserInfo()
        getGroupByGroupId()
        getLentDebts()
        getOweDebts()
    }

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

     fun refreshLentDebts() {
        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<ManualDebt>()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${userId}/lent/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val lentDebt = it.getValue(ManualDebt::class.java)!!
                            if (lentDebt.fromUser.uid == userId) {
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

          private fun getLentDebts(){
            val userId = FirebaseAuth.getInstance().uid
            val debtsArrayList = arrayListOf<ManualDebt>()
            val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${userId}/lent/")
            viewModelScope.launch(Dispatchers.IO) {
                reference.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                val lentDebt = it.getValue(ManualDebt::class.java)!!
                                if (lentDebt.fromUser.uid == userId) {
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

     private fun getOweDebts(){
         val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<ManualDebt>()
         debtsArrayList.clear()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${userId}/owe/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val oweDebt = it.getValue(ManualDebt::class.java)!!
                            if (oweDebt.toUser.uid == userId) {
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

     fun refreshOweDebts(){
        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<ManualDebt>()
        debtsArrayList.clear()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${userId}/owe/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val oweDebt = it.getValue(ManualDebt::class.java)!!
                            if (oweDebt.toUser.uid == userId) {
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

    private fun updateOweDebts(){
        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<ManualDebt>()
        debtsArrayList.clear()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${userId}/owe/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val oweDebt = it.getValue(ManualDebt::class.java)!!
                            if (oweDebt.toUser.uid == userId) {
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

    private fun updateLentDebts() {
        val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<ManualDebt>()
        debtsArrayList.clear()
        val reference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${userId}/lent/")
        viewModelScope.launch(Dispatchers.IO) {
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val lentDebt = it.getValue(ManualDebt::class.java)!!
                            if (lentDebt.fromUser.uid == userId) {
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

    fun createRequest(debt: ManualDebt){
        val pattern = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val newCalendar = Calendar.getInstance(Locale.getDefault())
        val neededDate = simpleDateFormat.format(newCalendar.time)
        viewModelScope.launch(Dispatchers.IO) {
            val lentReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${debt.fromUser.uid}/lent/")
            val oweReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${debt.toUser.uid}/owe/")
            val referenceAddRequested = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("requests/${debt.fromUser.uid}/requested")
            val referenceAddPending = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("requests/${debt.toUser.uid}/pending")

            oweReference.child(debt.debtId).removeValue().apply {
                updateOweDebts()
            }
            lentReference.child(debt.debtId).removeValue().apply {
                updateLentDebts()
            }
            referenceAddPending.push().setValue(Request(debt, neededDate))
            referenceAddRequested.push().setValue(Request(debt, neededDate))
        }
    }
}