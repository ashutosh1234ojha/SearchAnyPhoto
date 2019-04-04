package com.example.myapplication.data.localstore

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.example.myapplication.data.networkResponse.Photo

@Entity(tableName = "PhotoData")
data class PhotoData (

    @PrimaryKey
    @ColumnInfo(name = "search_text")
    var searchText: String = String(),

    var images:MutableList<Photo>? = ArrayList()
)