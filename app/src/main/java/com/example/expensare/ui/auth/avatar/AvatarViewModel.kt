package com.example.expensare.ui.auth.avatar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensare.R
import com.example.expensare.data.Avatar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AvatarViewModel: ViewModel() {
    private var _avatarList = MutableLiveData<ArrayList<Avatar>>()
    val avatarList: LiveData<ArrayList<Avatar>> get() = _avatarList

    init {
        getAvatars()
    }

    private fun getAvatars() {
        viewModelScope.launch(Dispatchers.IO) {
            val imagesArrayList = arrayListOf<Avatar>()
             val images = intArrayOf(
                R.drawable.avatar1,
                R.drawable.avatar2,
                R.drawable.avatar3,
                R.drawable.avatar4,
                R.drawable.avatar5,
                R.drawable.avatar6,
                R.drawable.avatar7,
                R.drawable.avatar8,
                R.drawable.avatar9,
                R.drawable.avatar10,
                R.drawable.avatar11,
                R.drawable.avatar12,
                R.drawable.avatar13,
                R.drawable.avatar14,
                R.drawable.avatar15,
                R.drawable.avatar16,
                R.drawable.avatar17,
                R.drawable.avatar18,
                R.drawable.avatar19,
                R.drawable.avatar20,
                R.drawable.avatar21,
                R.drawable.avatar22,
                R.drawable.avatar23,
                R.drawable.avatar24,
                R.drawable.avatar25,
                R.drawable.avatar26,
                R.drawable.avatar27,
                R.drawable.avatar28
            )
            images.forEach {
                imagesArrayList.add(Avatar(it))
            }
            _avatarList.postValue(imagesArrayList)
        }
    }
}