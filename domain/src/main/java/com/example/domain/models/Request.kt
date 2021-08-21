package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Request(val debt: Debt, val date: String) : Parcelable {
  constructor() : this(Debt(), "")
}
