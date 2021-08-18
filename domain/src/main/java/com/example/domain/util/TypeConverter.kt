package com.example.presentation.util

import androidx.room.TypeConverter
import com.example.domain.database.entities.UserEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object Converters {
  @TypeConverter
  fun fromString(value: String?): ArrayList<String> {
    val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
    return Gson().fromJson(value, listType)
  }

  @TypeConverter
  fun fromArrayList(list: ArrayList<String?>?): String {
    val gson = Gson()
    return gson.toJson(list)
  }

  @TypeConverter fun fromList(value: List<UserEntity>?) = Gson().toJson(value)

  @TypeConverter
  fun toList(value: String) = Gson().fromJson(value, Array<UserEntity>::class.java).toList()
}
