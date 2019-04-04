package com.example.myapplication.data.networkResponse

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class CommonResponse (

        @SerializedName("photos")
        @Expose
        var photos: Photos? = null,
        @SerializedName("stat")
        @Expose
        var stat: String? = null

)