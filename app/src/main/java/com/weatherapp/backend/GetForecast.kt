package com.weatherapp.backend

import android.content.Context
import com.weatherapp.backend.configuration.BackendErrorHandler
import com.weatherapp.backend.configuration.BackendException
import com.weatherapp.backend.configuration.BaseCall
import com.weatherapp.backend.configuration.ServerCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetForecast<T> : BaseCall<T> {

    @Throws(BackendException::class)
    constructor(context: Context?, URL: String?, checkInternetConnection: Boolean,
                appId: String, latitude: Double, longitude: Double,
                serverCallback: ServerCallback<*>?): super(URL, context, checkInternetConnection) {

        val call = apiService!!.getForecast(headers, appId, latitude, longitude) as Call<T?>? // Setup call

        call?.enqueue(object : Callback<T?> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                val statusCode: Int = response.code() // response code
                if (serverCallback != null) {
                    if (statusCode >= 200 && statusCode <= 299) { // Successful response
                        serverCallback.onResponse(statusCode, response as Response<*>)
                    } else { // failed response
                        BackendErrorHandler(statusCode, serverCallback)
                    }
                }
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                // Log error here since request failed
                if (serverCallback != null) {
                    serverCallback.onFailure(0, t)
                }
            }
        })

    }
}

