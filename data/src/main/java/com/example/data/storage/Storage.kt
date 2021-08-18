package com.example.data.storage

import android.content.Context
import android.content.SharedPreferences

private const val KEY_GROUP_ID = "GROUP_ID"

class Storage(context: Context) {

  private val sharedPreferences: SharedPreferences =
    context.getSharedPreferences("storage", Context.MODE_PRIVATE)

  var groupId: String
    get() = sharedPreferences.getString(KEY_GROUP_ID, "def").toString()
    set(value) {
      sharedPreferences.edit().putString(KEY_GROUP_ID, value).apply()
    }
}
