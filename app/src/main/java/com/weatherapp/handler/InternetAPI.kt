package com.weatherapp.handler

import android.content.Context
import android.net.ConnectivityManager

class InternetAPI {
    companion object {
        fun isConnected(context: Context): Boolean {
            val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }
}