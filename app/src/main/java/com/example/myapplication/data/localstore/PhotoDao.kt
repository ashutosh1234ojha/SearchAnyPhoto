package com.example.myapplication.data.localstore

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(photo: PhotoData)

    @Query("SELECT * FROM PhotoData WHERE search_text LIKE :searchText")
    fun loadAllByIds(searchText: String): MutableList<PhotoData>?

    @Query("SELECT search_text FROM PhotoData")
    fun loadSearchText(): MutableList<String>
}