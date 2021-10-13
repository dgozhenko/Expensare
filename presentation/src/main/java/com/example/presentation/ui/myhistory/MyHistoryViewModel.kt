package com.example.presentation.ui.myhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.interactors.history.GetLentHistory
import com.example.data.interactors.history.GetOweHistory
import com.example.domain.models.Debt
import com.example.domain.models.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class MyHistoryViewModel @Inject constructor(
    private val getLentHistory: GetLentHistory,
    private val getOweHistory: GetOweHistory
) : ViewModel() {

    private val _lentHistory = MutableLiveData<Response<ArrayList<Debt>>>()
    val lentHistory: LiveData<Response<ArrayList<Debt>>>
        get() = _lentHistory

    private val _oweHistory = MutableLiveData<Response<ArrayList<Debt>>>()
    val oweHistory: LiveData<Response<ArrayList<Debt>>>
        get() = _oweHistory

    init {
        getLentHistory()
        getOweHistory()
    }

    private fun getLentHistory() {
        viewModelScope.launch(Dispatchers.Main) {
            getLentHistory.invoke().observeForever { _lentHistory.postValue(it) }
        }
    }

    private fun getOweHistory() {
        viewModelScope.launch(Dispatchers.Main) {
            getOweHistory.invoke().observeForever { _oweHistory.postValue(it) }
        }
    }
}