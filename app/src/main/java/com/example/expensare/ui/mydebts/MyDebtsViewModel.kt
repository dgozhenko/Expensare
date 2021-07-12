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

    fun createRequest(debt: ManualDebt){
        var oweKey = ""
        var lentKey = ""
        val lentReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${debt.fromUser.uid}/lent/")
        val oweReference = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/manual_debts/${debt.toUser.uid}/owe/")
        val referenceCheck = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("requests/${debt.toUser.uid}/")
        val referenceAddRequested = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("requests/${debt.fromUser.uid}/requested")
        val referenceAddPending = FirebaseDatabase.getInstance("https://expensare-default-rtdb.europe-west1.firebasedatabase.app/").getReference("requests/${debt.toUser.uid}/pending")
        viewModelScope.launch(Dispatchers.IO) {
            oweReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val oweDebt = it.getValue(ManualDebt::class.java)!!
                            Log.d("oweDebt", oweDebt.toString())
                            if (oweDebt.debtId == debt.debtId) {
                                oweKey = it.key.toString()
                                lentReference.addListenerForSingleValueEvent(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            snapshot.children.forEach {
                                                val lentDebt = it.getValue(ManualDebt::class.java)!!
                                                Log.d("lentDebt", lentDebt.toString())
                                                if (lentDebt.debtId == debt.debtId) {
                                                    lentKey = it.key.toString()
                                                    referenceCheck.addListenerForSingleValueEvent(object : ValueEventListener {
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                            lentReference.child(lentKey).removeValue()
                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            TODO("Not yet implemented")
                                                        }
                                                    })
                                                }
                                            }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                                referenceCheck.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        referenceAddPending.push().setValue(Request(oweKey, debt.debtId, debt.toUser, debt.fromUser, debt.amount, debt.debtFor, debt.date))
                                        referenceAddRequested.push().setValue(Request(oweKey, debt.debtId, debt.toUser, debt.fromUser, debt.amount, debt.debtFor, debt.date))
                                            oweReference.child(oweKey).removeValue()
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
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
}