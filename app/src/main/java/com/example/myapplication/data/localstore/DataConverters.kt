package com.example.myapplication.data.localstore

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.myapplication.data.networkResponse.Photo

class DataConverters {

    @TypeConverter
    fun fromArrayList(list: MutableList<Photo>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromString(value: String): MutableList<Photo>? {
        val listType = object : TypeToken<MutableList<Photo>?>() {}.type
        return Gson().fromJson(value, listType)
    }


}