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
import com.example.domain.models.GroupList
import com.example.domain.models.util.Response
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

    private val _groceryList = MutableLiveData<Response<ArrayList<GroupList>>>()
    val groceryGroupList: LiveData<Response<ArrayList<GroupList>>>
        get() = _groceryList

    private val _refreshedGroceryList = MutableLiveData<Response<ArrayList<GroupList>>>()
    val refreshedGroceryGroupList: LiveData<Response<ArrayList<GroupList>>>
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

    fun deleteGroceryItem(group: GroupList) {
        viewModelScope.launch(Dispatchers.Main) {
            deleteListItem.invoke(group).observeForever {
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
