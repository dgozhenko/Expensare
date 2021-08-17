package com.example.presentation.ui.auth.avatar

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Avatar
import com.google.firebase.auth.FirebaseAuth
import com.inner_circles_apps.myapplication.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvatarViewModel @Inject constructor(): ViewModel() {
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
                imagesArrayList.add(Avatar(Uri.parse("android.resource://com.example.expensare/drawable/$it"), true))
            }
            uploadExclusiveAvatars(imagesArrayList)
            _avatarList.postValue(imagesArrayList)
        }
    }
    // TODO: 17.08.2021 Repository
    private fun uploadExclusiveAvatars(imagesArrayList: ArrayList<Avatar>) {
        if (FirebaseAuth.getInstance().uid == "ZukjAziJ56ezbm57PqxVL2dQZZa2") {
            imagesArrayList.add(Avatar(Uri.parse("android.resource://com.example.expensare/drawable/" + R.drawable.exclusive_avatar), true))
        } else if (FirebaseAuth.getInstance().uid == "HzezA120ZgP1KjMk4zt63mjxrbC3") {
            imagesArrayList.add(Avatar(Uri.parse("android.resource://com.example.expensare/drawable/" + R.drawable.exclusive_avaatar2), true))
        }
    }
}