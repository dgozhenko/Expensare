package com.example.presentation.ui.requests

import androidx.lifecycle.*
import com.example.data.interactors.requests.acceptHandler
import com.example.data.interactors.requests.getPendingRequests
import com.example.data.interactors.requests.getRequestedRequests
import com.example.data.interactors.user.DownloadUser
import com.example.domain.models.Debt
import com.example.domain.models.Request
import com.example.domain.models.User
import com.example.domain.models.util.Response
import com.example.domain.models.util.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import java.util.*


sealed class acceptHandlerResult {
    object Success : acceptHandlerResult()
    data class Error(val exception: Exception) : acceptHandlerResult()
}

@HiltViewModel
class RequestsViewModel @Inject constructor(
    private val downloadUser: DownloadUser,
    private val getPendingRequests: getPendingRequests,
    private val getRequestedRequests: getRequestedRequests,
    private val acceptHandler: acceptHandler
) : ViewModel() {

    private val _user = MutableLiveData<Response<User>>()
    val user: LiveData<Response<User>>
        get() = _user

    private val _requestedRequests = MutableLiveData<Response<ArrayList<Request>>>()
    val requestedRequests: LiveData<Response<ArrayList<Request>>>
        get() = _requestedRequests

    private val _pendingRequests = MutableLiveData<Response<ArrayList<Request>>>()
    val pendingRequests: LiveData<Response<ArrayList<Request>>>
        get() = _pendingRequests

    private val _acceptHandlerResult: SingleLiveEvent<Response<String>> = SingleLiveEvent()
    val handlerResult: LiveData<Response<String>>
        get() = _acceptHandlerResult

    init {
        getUserInfo()
        getPendingRequests()
        getRequestedRequests()
    }

    // TODO: 17.08.2021 Repository
    private fun getUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            downloadUser.invoke().observeForever { _user.postValue(it) }
        }
    }

    // TODO: 17.08.2021 Repository
    fun getPendingRequests() {
        viewModelScope.launch(Dispatchers.Main) {
            getPendingRequests.invoke().observeForever { _pendingRequests.postValue(it) }
        }
    }

    // TODO: 17.08.2021 Repository
    fun getRequestedRequests() {
        viewModelScope.launch(Dispatchers.Main) {
            getRequestedRequests.invoke().observeForever { _requestedRequests.postValue(it) }
        }
    }

    // TODO: 17.08.2021 Repository
    fun acceptHandler(request: Request, choice: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            acceptHandler.invoke(request, choice).observeForever { _acceptHandlerResult.postValue(it) }
        }
    }

}