package com.weatherapp.model.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Forecast {
    @SerializedName("cod")
    @Expose
    val cod: String? = null

    @SerializedName("message")
    @Expose
    val message: Double? = null

    @SerializedName("cnt")
    @Expose
    val cnt: Long? = null

    @SerializedName("list")
    @Expose
    val weatherData: List<WeatherData>? = null

}