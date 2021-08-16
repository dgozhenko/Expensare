package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Request(val debt: ManualDebt, val date: String) : Parcelable {
    constructor(): this (ManualDebt(), "")
}