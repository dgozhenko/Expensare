package com.example.domain.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Avatar (val avatar: Uri, val fromAvatarPicker: Boolean) : Parcelable