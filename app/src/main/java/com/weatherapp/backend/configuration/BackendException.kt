package com.weatherapp.backend.configuration

import com.weatherapp.handler.AppException

class BackendException : AppException {
    constructor(message: String?) : super(message)
}