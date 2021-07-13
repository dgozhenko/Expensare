package com.example.expensare.ui.mydebts

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensare.data.models.Group
import com.example.expensare.data.models.ManualDebt
import com.example.expensare.data.models.Request
import com.example.expensare.data.models.User
import com.example.expensare.ui.storage.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyDebtsViewModel(private val getApplication: Application) : AndroidViewModel(getApplication) {

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

    init {
        getUserInfo()
        getGroupByGroupId()
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

         fun getLentDebts(){
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
                            _lentDebts.postValue(debtsArrayList)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

     fun getOweDebts(){
         val userId = FirebaseAuth.getInstance().uid
        val debtsArrayList = arrayListOf<ManualDebt>()
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
                        _oweDebts.postValue(debtsArrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun deleteLent(debt: ManualDebt){
        val lentReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${debt.fromUser.uid}/lent/")
        val lentDeleteReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${debt.fromUser.uid}/lent/")
        Log.d("lentDebt", debt.fromUser.uid)
        lentReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val lentDebt = it.getValue(ManualDebt::class.java)!!
                        if (lentDebt.debtId == debt.debtId) {
                            val lentKey = it.key
                            Log.d("lentDebt", lentKey.toString())
                            lentDeleteReference.child("$lentKey/").removeValue()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    fun createRequest(debt: ManualDebt){
        var oweKey = ""
        var lentKey = ""

        val lentReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${debt.fromUser.uid}/lent/")
        val oweReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${debt.toUser.uid}/owe/")
        val referenceAddRequested = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("requests/${debt.fromUser.uid}/requested")
        val referenceAddPending = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("requests/${debt.toUser.uid}/pending")

        val pattern = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val newCalendar = Calendar.getInstance(Locale.getDefault())
        val neededDate = simpleDateFormat.format(newCalendar.time)

        viewModelScope.launch(Dispatchers.IO) {
            oweReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val oweDebt = it.getValue(ManualDebt::class.java)!!
                            Log.d("oweDebt", oweDebt.toString())
                            if (oweDebt.debtId == debt.debtId) {
                                oweKey = it.key!!

                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            lentReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val lentDebt = it.getValue(ManualDebt::class.java)!!
                            if (lentDebt.debtId == debt.debtId) {
                                lentKey = it.key!!
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            lentReference.child("$lentKey/").removeValue()
            oweReference.child("$oweKey/").removeValue()
            referenceAddPending.push().setValue(Request(debt,neededDate))
            referenceAddRequested.push().setValue(Request(debt,neededDate))
        }
    }
}