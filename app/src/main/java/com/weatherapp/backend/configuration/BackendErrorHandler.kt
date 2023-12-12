package com.weatherapp.backend.configuration

import com.weatherapp.applicationmanger.Constants
import java.lang.Exception

class BackendErrorHandler {
    constructor(statusCode: Int, serverCallback: ServerCallback<*>) {
        if (statusCode == 404) {
            serverCallback.onFailure(statusCode, Throwable(Constants.API_NOT_FOUND_PLEASE_TRY_LATER))
        } else if (statusCode == 429) {
            readErrorFromRespondObj(statusCode, serverCallback, Constants.TOO_MANY_REQUESTS)
        } else {
            readErrorFromRespondObj(statusCode, serverCallback, Constants.SOMETHING_WENT_WRONG)
        }
    }

    private fun readErrorFromRespondObj(
        statusCode: Int, serverCallback: ServerCallback<*>,
        defaultErrorMessage: String
    ) {
        try {
            serverCallback.onFailure(statusCode, Throwable(defaultErrorMessage))
        } catch (e: Exception) {
            serverCallback.onFailure(statusCode, Throwable(e.message))
        }
    }
}