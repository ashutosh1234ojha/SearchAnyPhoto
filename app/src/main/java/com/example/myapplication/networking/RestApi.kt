package com.example.myapplication.networking

import com.example.myapplication.data.networkResponse.CommonResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RestApi {

    @GET("rest/")
    fun getImages(@QueryMap map: Map<String, String>): Call<CommonResponse>
}
