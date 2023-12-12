package com.weatherapp.model.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.weatherapp.model.weather.Weather

class WeatherData {
    @SerializedName("dt")
    @Expose
    var dt: Long? = null

    @SerializedName("main")
    @Expose
    var main: Main? = null

    @SerializedName("weather")
    @Expose
    var weather: List<Weather>? = null

    @SerializedName("dt_txt")
    @Expose
    var dtTxt: String? = null

    @SerializedName("rain")
    @Expose
    var rain: Rain? = null

    constructor(dt: Long?, weather: List<Weather>?, dtTxt: String?) {
        this.dt = dt
        this.weather = weather
        this.dtTxt = dtTxt
    }
}