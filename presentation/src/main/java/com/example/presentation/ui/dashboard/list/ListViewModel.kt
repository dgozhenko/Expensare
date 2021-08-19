package com.example.presentation.ui.dashboard.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.interactors.group.GetGroupByGroupId
import com.example.data.interactors.list.DeleteListItem
import com.example.data.interactors.list.GetList
import com.example.data.storage.Storage
import com.example.domain.models.Group
import com.example.domain.models.ListItem
import com.example.domain.models.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class ListViewModel @Inject constructor(private val storage: Storage,
                                        private val deleteListItem: DeleteListItem,
                                        private val getList: GetList,
                                        private val getGroupByGroupId: GetGroupByGroupId) : ViewModel() {

    private val _group = MutableLiveData<Response<Group>>()
    val group: LiveData<Response<Group>>
        get() = _group

    private val _groceryList = MutableLiveData<Response<ArrayList<ListItem>>>()
    val groceryList: LiveData<Response<ArrayList<ListItem>>>
        get() = _groceryList

    private val _refreshedGroceryList = MutableLiveData<Response<ArrayList<ListItem>>>()
    val refreshedGroceryList: LiveData<Response<ArrayList<ListItem>>>
        get() = _refreshedGroceryList

    private val _deleteResult = MutableLiveData<Response<String>>()
    val deleteResult: LiveData<Response<String>> get() = _deleteResult

    init {
        getGroupByGroupId()
        getGroupGroceryList()
    }

    fun refreshGroceryList() {
        viewModelScope.launch(Dispatchers.Main) {
            getList.invoke().observeForever {
                _refreshedGroceryList.postValue(it)
            }
        }
    }

    fun deleteGroceryItem(item: ListItem) {
        viewModelScope.launch(Dispatchers.Main) {
            deleteListItem.invoke(item).observeForever {
                _deleteResult.postValue(it)
            }
        }
    }

    private fun getGroupGroceryList() {
        viewModelScope.launch(Dispatchers.Main) {
            getList.invoke().observeForever {
                _groceryList.postValue(it)
            }
        }
    }

    private fun getGroupByGroupId() {
        viewModelScope.launch(Dispatchers.Main) {
            getGroupByGroupId.invoke().observeForever {
                _group.postValue(it)
            }
        }
    }
}
