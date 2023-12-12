package com.weatherapp.handler

import java.lang.Exception

open class AppException : Exception{
    constructor(message: String?) : super(message)
}