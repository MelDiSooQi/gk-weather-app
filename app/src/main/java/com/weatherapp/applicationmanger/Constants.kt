package com.weatherapp.applicationmanger

import com.weatherapp.BuildConfig


class Constants {

    companion object {
        // API KEYS
        const val OPEN_WEATHER_API_KEY = BuildConfig.OPEN_WEATHER_API_KEY
        const val MAPS_API_KEY = BuildConfig.MAPS_API_KEY

        // Main and base URL
        const val MAIN_URL = BuildConfig.API_BASE_URL
        const val BASE_URL = "$MAIN_URL/"

        //    ==============================================================================================
        // Headers and types
        const val CONTENT_TYPE = "Content-Type"
        const val CONTENT_TYPE_APPLICATION_JSON = "application/json"

        // ERRORS HANDLING START
        const val CHECK_INTERNET_CONNECTION = "Check Internet Connection!"
        const val API_NOT_FOUND_PLEASE_TRY_LATER = "API Not Found. Please, Try later!"
        const val TOO_MANY_REQUESTS = "Too Many Requests!"
        const val SOMETHING_WENT_WRONG = "Something went wrong!"

        // ERRORS HANDLING END
        //    ==============================================================================================
        //SharedPreferences
        const val LAST_UPDATED_TIME = "lastUpdatedTime"

    }
}