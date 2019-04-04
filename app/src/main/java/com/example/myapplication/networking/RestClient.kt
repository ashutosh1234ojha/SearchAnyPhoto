package com.example.myapplication.networking

import com.example.myapplication.utility.Constants

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {
    private var REST_CLIENT: RestApi? = null


    init {
        initRestClient()
    }

    fun get(): RestApi? {
        return REST_CLIENT
    }

    private fun initRestClient() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient()).build()
        REST_CLIENT = retrofit.create(RestApi::class.java)
    }

    /**
     * @return object of OkHttpClient
     */
    private fun getOkHttpClient(): OkHttpClient {

        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor(getLoggingInterceptor())
        return httpClient.build()
    }


    /**
     * Method to get object of HttpLoggingInterceptor
     *
     * @return object of HttpLoggingInterceptor
     */
    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }
}

