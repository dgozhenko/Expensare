package com.example.data.storage

import android.content.Context
import android.content.SharedPreferences

private const val KEY_GROUP_ID = "GROUP_ID"
private const val KEY_USER_EMAIL = "USER_EMAIL"
private const val KEY_AVATAR = "AVATAR"



class Storage(context: Context) {

  private val sharedPreferences: SharedPreferences =
    context.getSharedPreferences("storage", Context.MODE_PRIVATE)

  var groupId: String
    get() = sharedPreferences.getString(KEY_GROUP_ID, "def").toString()
    set(value) {
      sharedPreferences.edit().putString(KEY_GROUP_ID, value).apply()
    }

  var userEmail: String
  get() = sharedPreferences.getString(KEY_USER_EMAIL, "def").toString()
  set(value) {
    sharedPreferences.edit().putString(KEY_USER_EMAIL, value).apply()
  }

  var userAvatar: String
  get() = sharedPreferences.getString(KEY_AVATAR, "src/main/res/drawable-v24/ic_launcher_foreground.xml").toString()
  set(value) = sharedPreferences.edit().putString(KEY_AVATAR, value).apply()
}
