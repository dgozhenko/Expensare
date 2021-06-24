package com.example.expensare.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Input(
    val Avatar: Avatar,
    val email: String,
) : Parcelable
