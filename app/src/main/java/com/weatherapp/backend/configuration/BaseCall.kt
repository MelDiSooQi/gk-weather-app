package com.weatherapp.backend.configuration

import android.content.Context
import com.weatherapp.applicationmanger.Constants
import com.weatherapp.handler.InternetAPI
import java.util.HashMap

open class BaseCall<T> {
    protected var apiService: ApiInterface<T>? = null
    protected var headers: HashMap<String, String>? = null

    @Throws(BackendException::class)
    constructor(url: String?, context: Context?, checkInternetConnection: Boolean) {
        if (context != null) {
            if (checkInternetConnection) {
                if (!InternetAPI.isConnected(context)) {
                    throw BackendException(Constants.CHECK_INTERNET_CONNECTION)
                }
            }
        }
        apiService = ApiClient.getClient(url)?.create(ApiInterface::class.java) as ApiInterface<T>?
        headers = HashMap()
        headers!![Constants.CONTENT_TYPE] = Constants.CONTENT_TYPE_APPLICATION_JSON
    }
}