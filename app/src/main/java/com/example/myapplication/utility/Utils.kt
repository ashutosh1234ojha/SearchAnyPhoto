package com.example.myapplication.utility

import android.content.Context
import android.net.ConnectivityManager

object Utils {

    fun isConnectedToInternet(context: Context): Boolean {
        return try {
            val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            mNetworkInfo != null
        } catch (e: NullPointerException) {
            false
        }
    }
}
