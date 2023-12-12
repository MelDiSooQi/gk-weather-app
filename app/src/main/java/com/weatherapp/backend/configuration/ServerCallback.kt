package com.weatherapp.backend.configuration

import retrofit2.Response

interface ServerCallback<T> {
    /**
     * Invoked for a received HTTP response.
     *
     *
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call [Response.isSuccessful] to determine if the response indicates success.
     */
    fun onResponse(statusCode: Int, response: Response<*>)


    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     */
    fun onFailure(statusCode: Int, throwable: Throwable?)
}