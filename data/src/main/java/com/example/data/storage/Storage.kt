package com.example.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.data.R
import com.example.domain.util.Constants

class Storage(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("storage", Context.MODE_PRIVATE)

    var groupId: String
        get() = sharedPreferences.getString(Constants.KEY_GROUP_ID, "def").toString()
        set(value) {
            sharedPreferences.edit().putString(Constants.KEY_GROUP_ID, value).apply()
        }

    var userEmail: String
        get() = sharedPreferences.getString(Constants.KEY_USER_EMAIL, "def").toString()
        set(value) {
            sharedPreferences.edit().putString(Constants.KEY_USER_EMAIL, value).apply()
        }

    var userAvatar: String
        get() =
            sharedPreferences
                .getString(
                    Constants.KEY_AVATAR,
                    "def").toString()
        set(value) = sharedPreferences.edit().putString(Constants.KEY_AVATAR, value).apply()
}
