package com.example.data.storage

import android.content.Context
import android.content.SharedPreferences

private const val KEY_GROUP_ID = "GROUP_ID"

class Storage(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("storage", Context.MODE_PRIVATE)

    var groupId: String
        get() =
            sharedPreferences
                .getString(KEY_GROUP_ID, "a7b3844f-6b0d-4092-9dc2-14b0c3b2c68d")
                .toString()
        set(value) {
            sharedPreferences.edit().putString(KEY_GROUP_ID, value).apply()
        }
}
